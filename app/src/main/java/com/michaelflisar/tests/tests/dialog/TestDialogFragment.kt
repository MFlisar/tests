package com.michaelflisar.tests.tests.dialog

import android.app.Dialog
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.fragment.app.DialogFragment
import com.michaelflisar.lumberjack.L
import com.michaelflisar.tests.databinding.DialogFragmentTestBinding

class TestDialogFragment : DialogFragment() {

    companion object {
        fun newInstance(): TestDialogFragment {
            return TestDialogFragment()
        }
    }

    lateinit var binding: DialogFragmentTestBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        super.onCreateView(inflater, container, savedInstanceState)
        binding = DialogFragmentTestBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dlg = super.onCreateDialog(savedInstanceState)
        dlg.setOnShowListener {

            val window = dlg.window!!
            val root = window.decorView
            val content = dlg.findViewById<View>(android.R.id.content)
            val parent = root.parent

            L.d { "parent of root: $parent | ${parent is ViewGroup} | ${parent is View}" }
            L.d { "window.container: ${window.container}" }
            L.d { "window.context: ${window.context}" }
            L.d { "window.attributes: ${window.attributes}" }
            L.d { "window.windowManager: ${window.windowManager}" }
            L.d { "window.windowStyle: ${window.windowStyle}" }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                L.d { "window.insetsController: ${window.insetsController}" }
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                L.d { "window.rootSurfaceControl: ${window.rootSurfaceControl}" }
            }

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP_MR1) {
                window.setClipToOutline(false)
            }

            root.clipToOutline = false
            (root as ViewGroup).let {
                it.clipChildren = false
                it.clipToPadding = false
            }

            root.translationY = 2000f
            root
                .animate()
                .translationY(0f)
                .setDuration(2000)
                .start()
        }
        return dlg
    }

}