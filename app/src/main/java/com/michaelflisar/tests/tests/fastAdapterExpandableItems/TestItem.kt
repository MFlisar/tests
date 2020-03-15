package com.michaelflisar.tests.tests.fastAdapterExpandableItems

import android.view.View
import androidx.databinding.DataBindingUtil
import com.michaelflisar.tests.R
import com.michaelflisar.tests.databinding.ItemTestBinding
import com.mikepenz.fastadapter.FastAdapter
import com.mikepenz.fastadapter.IExpandable
import com.mikepenz.fastadapter.IParentItem
import com.mikepenz.fastadapter.ISubItem
import com.mikepenz.fastadapter.items.AbstractItem

class TestItem(
        val info: String,
        val level: Int,
        val parentItem: TestItem?,
        val childItems: MutableList<TestItem>
) : AbstractItem<TestItem.ViewHolder>(), IExpandable<TestItem.ViewHolder> {

    override val type: Int = R.layout.item_test
    override val layoutRes: Int = R.layout.item_test

    private var expanded = false

    override val isAutoExpanding: Boolean = true
    override var isExpanded: Boolean
        get() = expanded
        set(value) {
            expanded = value
        }
    override var parent: IParentItem<*>?
        get() = parentItem
        set(value) {}
    override var subItems: MutableList<ISubItem<*>>
        get() = childItems as MutableList<ISubItem<*>>
        set(value) {}

    override fun getViewHolder(v: View): ViewHolder {
        return ViewHolder(v)
    }

    class ViewHolder(view: View) : FastAdapter.ViewHolder<TestItem>(view) {

        var binding: ItemTestBinding = DataBindingUtil.bind(view)!!

        override fun bindView(item: TestItem, payloads: List<Any>) {

            var text = ""
            for (i in 0..item.level) {
                text += "  "
            }
            text += item.info

            // 1) Name
            binding.tvText1.text = text
        }

        override fun unbindView(item: TestItem) {
            binding.tvText1.text = ""
            binding.tvText2.text = ""
        }
    }
}