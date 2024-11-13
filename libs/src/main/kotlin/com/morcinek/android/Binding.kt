package com.morcinek.android

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding


class BindingViewHolder<B : ViewBinding>(val binding: B) : RecyclerView.ViewHolder(binding.root)

abstract class BAdapter<T, B : ViewBinding>(private val createBinding: (LayoutInflater, ViewGroup?, Boolean) -> B) : RecyclerView.Adapter<BindingViewHolder<B>>() {

    private var onCreateView: (B.() -> Unit) = { }
    private var onBindView: (B.(Int, T) -> Unit) = { _, _ -> }
    private var onBindPayloads: (B.(Int, T, MutableList<Any>) -> Unit) = { _, _, _ -> }
    private var onAttached: (B.() -> Unit) = { }
    private var onDetached: (B.() -> Unit) = { }

    protected abstract fun getItem(position: Int): T

    fun onCreateView(onCreateView: B.() -> Unit) {
        this.onCreateView = onCreateView
    }

    fun onBind(onBindView: B.(position: Int, item: T) -> Unit) {
        this.onBindView = onBindView
    }

    fun onBindPayloads(onBindPayloads: B.(position: Int, item: T, payloads: MutableList<Any>) -> Unit) {
        this.onBindPayloads = onBindPayloads
    }

    fun onAttached(onAttached: B.() -> Unit) {
        this.onAttached = onAttached
    }

    fun onDetached(onDetached: B.() -> Unit) {
        this.onDetached = onDetached
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = BindingViewHolder(createBinding(LayoutInflater.from(parent.context), parent, false).apply(onCreateView))

    override fun onBindViewHolder(holder: BindingViewHolder<B>, position: Int) = holder.binding.onBindView(position, getItem(position))

    override fun onBindViewHolder(holder: BindingViewHolder<B>, position: Int, payloads: MutableList<Any>) =
        if (payloads.isEmpty()) onBindViewHolder(holder, position)
        else holder.binding.onBindPayloads(position, getItem(position), payloads)

    override fun onViewAttachedToWindow(holder: BindingViewHolder<B>) = holder.binding.onAttached()

    override fun onViewDetachedFromWindow(holder: BindingViewHolder<B>) = holder.binding.onDetached()
}
