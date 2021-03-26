package com.michaelflisar.tests.tests.coil.display

import android.graphics.Color
import android.graphics.PorterDuff
import android.graphics.PorterDuffColorFilter
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.toBitmap
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import coil.request.CachePolicy
import com.bumptech.glide.Glide
import com.michaelflisar.tests.R
import com.michaelflisar.tests.databinding.DialogAppItemBinding
import com.michaelflisar.tests.databinding.DialogAppItemSepBinding
import com.michaelflisar.tests.databinding.FragmentImageDisplayTestBinding
import com.michaelflisar.tests.tests.TestActivity
import com.michaelflisar.tests.tests.coil.ImageManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class ImageDisplayFragment : Fragment() {

    companion object {
        val DARK_THEME = false
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val binding = FragmentImageDisplayTestBinding.inflate(inflater)
        initLayout(binding)
        return binding.root
    }

    fun initLayout(binding: FragmentImageDisplayTestBinding) {

        // 1) create a list of items
        val items = mutableListOf(
                Item(-1, "", Type.None),
                Item(R.drawable.ic_baseline_edit_24, "Rename", Type.Rename),
                Item(-1, "", Type.None),
                Item(R.drawable.ic_baseline_settings_24, "App Settings", Type.AppSettings),
                Item(R.drawable.ic_baseline_app_settings_alt_24, "System Settings", Type.SystemSettings)
        )

        for (i in 0..5) {
            items.add(i, Item(R.drawable.ic_baseline_create_new_folder_24, "Add Folder ${i + 1}", Type.Folder, i.toLong()))
        }

        // 2) convert items to views and add them to the root LinearLayout
        val layoutInflater = LayoutInflater.from(context)
        items
                .forEach { item ->
                    if (item.type == Type.None) {
                        val b = DialogAppItemSepBinding.inflate(layoutInflater, binding.llRoot, true)
                        b.root.setBackgroundColor(if (DARK_THEME) Color.LTGRAY else Color.DKGRAY)
                    } else {
                        val b = DialogAppItemBinding.inflate(layoutInflater, binding.llRoot, true)
                        b.tvLabel.text = item.text
                        initText(b.tvLabel)
                        initIcon(b.ivIcon)
                        initIcon(b.ivIcon2)
                        initIcon(b.ivIcon3)
                        initIcon(b.ivIcon4)
                        ImageManager.display(item.icon, b.ivIcon) {
                            //ColorTintTransformation(Color.BLACK)

                            memoryCachePolicy(CachePolicy.DISABLED)
                            diskCachePolicy(CachePolicy.DISABLED)
                        }
                        Glide
                                .with(b.ivIcon2)
                                .load(item.icon)
                                .into(b.ivIcon2)

                        // we don't care about correctly cancelling in this test here
                        lifecycleScope.launch(Dispatchers.IO) {
                            val drawable = ContextCompat.getDrawable(context!!, item.icon)!!
                            val bitmap = drawable.toBitmap() // does work the same way as coils implementation!
                            withContext(Dispatchers.Main) {
                                b.ivIcon3.setImageBitmap(bitmap)
                                b.ivIcon4.setImageDrawable(drawable)
                            }
                        }

                        b.root.setOnClickListener {
                            onClick(item)
                        }
                    }
                }
    }

    // -------------------
    // Helper functions
    // -------------------

    private fun initText(tv: TextView) {
        tv.setTextColor(if (DARK_THEME) Color.WHITE else Color.BLACK)
    }

    private fun initIcon(iv: ImageView) {
        val color = if (DARK_THEME) Color.LTGRAY else Color.DKGRAY
        val mode = PorterDuff.Mode.SRC_IN
        iv.colorFilter = PorterDuffColorFilter(color, mode)
    }

    private fun onClick(item: Item) {
        // close this demo on item click...
        (activity as TestActivity).onBackPressed()
    }

    // -------------------
    // Helper classes
    // -------------------

    class Item(
            val icon: Int,
            val text: String,
            val type: Type,
            val id: Long = 0L
    )

    enum class Type {
        Info,
        Hide,
        PlayStore,
        Uninstall,
        PinToTop,
        Rename,
        None,
        AppSettings,
        SystemSettings,
        Folder,
        RemoveFromFolder,
        DeleteFolder
    }
}
