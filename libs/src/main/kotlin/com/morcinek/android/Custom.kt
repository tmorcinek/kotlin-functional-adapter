package com.morcinek.android

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding

fun customAdapter(body: CAdapter.() -> Unit) = CAdapter().apply(body)

class CAdapter : RecyclerView.Adapter<BindingViewHolder<ViewBinding>>() {

    private val itemsTypes = mutableListOf<CItem<ViewBinding>>()

    fun <B : ViewBinding> item(createBinding: (LayoutInflater, ViewGroup?, Boolean) -> B, body: CItem<B>.() -> Unit = {}) =
        itemsTypes.add(CItem(createBinding).apply(body) as CItem<ViewBinding>)

    fun <B : ViewBinding> itemBinding(createBinding: (LayoutInflater, ViewGroup?, Boolean) -> B, onBindView: B.(position: Int) -> Unit) =
        item(createBinding) { onBind(onBindView) }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = itemsTypes[viewType].onCreateViewHolder(parent)

    override fun onBindViewHolder(holder: BindingViewHolder<ViewBinding>, position: Int) = itemsTypes[position].onBindViewHolder(holder, position)

    override fun getItemViewType(position: Int) = position

    override fun getItemCount() = itemsTypes.size
}

class CItem<B : ViewBinding>(private val createBinding: (LayoutInflater, ViewGroup?, Boolean) -> B) {

    private var onCreateView: (B.() -> Unit) = { }
    private var onBindView: (B.(Int) -> Unit) = { }

    fun onCreateView(onCreateView: B.() -> Unit) {
        this.onCreateView = onCreateView
    }

    fun onBind(onBindView: B.(position: Int) -> Unit) {
        this.onBindView = onBindView
    }

    internal fun onCreateViewHolder(parent: ViewGroup) =
        BindingViewHolder(createBinding(LayoutInflater.from(parent.context), parent, false).apply(onCreateView))

    internal fun onBindViewHolder(viewHolder: BindingViewHolder<B>, position: Int) = viewHolder.binding.onBindView(position)
}