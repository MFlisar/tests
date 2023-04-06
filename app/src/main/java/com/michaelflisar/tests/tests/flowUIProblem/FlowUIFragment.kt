package com.michaelflisar.tests.tests.flowUIProblem

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.michaelflisar.tests.databinding.FragmentFlowUiBinding
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlin.random.Random


class FlowUIFragment : Fragment() {

    private lateinit var binding: FragmentFlowUiBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentFlowUiBinding.inflate(inflater)

        // Init UI (Listeners)
        initUI()

        // Observe Data
        observeData()

        return binding.root
    }

    private fun initUI() {
        // Clicking a button increase/decreases the number
        // this will lead to a reemission of the new value to the flow
        val clickListener = View.OnClickListener { button ->
            val modifier = if (button == binding.btIncrease) 1 else -1
            val value = binding.tvValue.text.toString().toInt()
            GlobalScope.launch {
                updateData(value + modifier)
            }
        }
        listOf(
            binding.btIncrease,
            binding.btDecrease
        ).forEach {
            it.setOnClickListener(clickListener)
        }
    }

    private fun observeData() {
        // we want to use a flow because in a real application there may be cases where updates come
        // from somewhere else, from "outside" and we want this UI to react to it...
        // this simple example would work with a simply suspend function for the initial value as well
        // but we want to find out how to solve this specific problem
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                DemoDataStore.flow
                    .collect {
                        binding.tvValue.text = it.toString()
                    }
            }
        }
    }

    private suspend fun updateData(value: Int) {
        // this update will need between 100 and 1000ms,
        // this means, the user may be faster increasing/decreasing the value
        // than the update will finish and will emit a new value to the flow above
        // => Problems: endless updates, overwriting current user input, ...
        DemoDataStore.update(
            value,
            delayInMillis = Random.nextLong(100, 1000)
        )
    }
}