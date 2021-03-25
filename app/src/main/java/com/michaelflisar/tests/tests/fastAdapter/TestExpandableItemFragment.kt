package com.michaelflisar.tests.tests.fastAdapter

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.michaelflisar.tests.databinding.FragmentExpandableTestBinding
import com.mikepenz.fastadapter.FastAdapter
import com.mikepenz.fastadapter.IItem
import com.mikepenz.fastadapter.adapters.ItemAdapter
import com.mikepenz.fastadapter.expandable.getExpandableExtension

class TestExpandableItemFragment : Fragment() {

    lateinit var binding: FragmentExpandableTestBinding

    private lateinit var itemAdapter: ItemAdapter<IItem<*>>
    private lateinit var fastAdapter: FastAdapter<IItem<*>>

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentExpandableTestBinding.inflate(inflater)
        initViews(savedInstanceState)
        return binding.root
    }

    fun initViews(savedInstanceState: Bundle?) {

        // ItemAdapter
        itemAdapter = ItemAdapter()

        // FastAdapter
        fastAdapter = FastAdapter.with(itemAdapter)
                .withSavedInstanceState(savedInstanceState)

        fastAdapter.getExpandableExtension().apply {
            isOnlyOneExpandedItem = true
        }

        // RV
        binding.rvData.layoutManager = LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)
        binding.rvData.adapter = fastAdapter

        // Daten setzen
        updateAdapterItems()

    }

    fun updateAdapterItems() {
        val items = ArrayList<TestItem>()

        val item1 = TestItem("Test 1", 0, null, ArrayList()).apply {
            subItems.addAll(
                    listOf(
                            TestItem("Test 1.1", 1, this, ArrayList()).apply {
                                subItems.addAll(
                                        listOf(
                                                TestItem("Test 1.1.1", 2, this, ArrayList()),
                                                TestItem("Test 1.1.2", 2, this, ArrayList()),
                                                TestItem("Test 1.1.3", 2, this, ArrayList())
                                        )
                                )
                            },
                            TestItem("Test 1.2", 1, this, ArrayList())
                    )
            )
        }
        val item2 = TestItem("Test 2", 0, null, ArrayList())


        items.addAll(listOf(item1, item2))

        itemAdapter.set(items)
    }
}