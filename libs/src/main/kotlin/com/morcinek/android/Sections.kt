package com.morcinek.android

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import kotlin.reflect.KClass


fun RecyclerView.setupSections(body: FSectionsAdapter.() -> Unit) {
    layoutManager = LinearLayoutManager(context)
    adapter = sectionsAdapter(body)
}

fun sectionsAdapter(body: FSectionsAdapter.() -> Unit) = FSectionsAdapter().apply(body)

class FSectionsAdapter : ListAdapter<HasKey, BindingViewHolder<ViewBinding>>(itemCallback()) {

    private val sectionViewAdapters = mutableMapOf<Int, FSection<Any, ViewBinding>>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = sectionViewAdapters[viewType]!!.onCreateViewHolder(parent)

    override fun onBindViewHolder(holder: BindingViewHolder<ViewBinding>, position: Int) =
        sectionViewAdapters[holder.itemViewType]!!.onBindViewHolder(holder, getItem(position), position)

    override fun onBindViewHolder(holder: BindingViewHolder<ViewBinding>, position: Int, payloads: MutableList<Any>) =
        if (payloads.isEmpty()) onBindViewHolder(holder, position)
        else sectionViewAdapters[holder.itemViewType]!!.onBindViewHolder(holder, getItem(position), position, payloads)

    override fun onViewDetachedFromWindow(holder: BindingViewHolder<ViewBinding>) = sectionViewAdapters[holder.itemViewType]!!.onDetachViewHolder(holder)

    override fun onViewAttachedToWindow(holder: BindingViewHolder<ViewBinding>) = sectionViewAdapters[holder.itemViewType]!!.onAttachViewHolder(holder)

    override fun getItemViewType(position: Int) = getItemViewTypeForClass(getItem(position).javaClass)

    private fun getItemViewTypeForClass(type: Class<*>) = type.hashCode()

    fun <T : HasKey, B : ViewBinding> addSectionViewAdapter(type: KClass<T>, createBinding: (LayoutInflater, ViewGroup?, Boolean) -> B, body: FSection<T, B>.() -> Unit) =
        sectionViewAdapters.put(getItemViewTypeForClass(type.java), FSection<T, B>(createBinding).apply(body) as FSection<Any, ViewBinding>)

    inline fun <reified T : HasKey, B : ViewBinding> section(noinline createBinding: (LayoutInflater, ViewGroup?, Boolean) -> B, noinline body: FSection<T, B>.() -> Unit = {}) =
        addSectionViewAdapter(T::class, createBinding, body)

    inline fun <reified T : HasKey, B : ViewBinding> sectionBinding(
        noinline createBinding: (LayoutInflater, ViewGroup?, Boolean) -> B,
        noinline onBindView: B.(position: Int, item: T) -> Unit
    ) = section<T, B>(createBinding) { onBind(onBindView) }

    inline fun <reified T> itemAtPositionIsClass(position: Int) = getItemViewType(position) == T::class.java.hashCode()
}

class FSection<T, B : ViewBinding>(private val createBinding: (LayoutInflater, ViewGroup?, Boolean) -> B) {

    private var onCreateView: (B.() -> Unit) = { }
    private var onBindView: (B.(Int, T) -> Unit) = { _, _ -> }
    private var onBindPayloads: (B.(Int, T, MutableList<Any>) -> Unit) = { _, _, _ -> }
    private var onDetached: (B.() -> Unit) = { }
    private var onAttached: (B.() -> Unit) = { }


    fun onCreateView(onCreateView: B.() -> Unit) {
        this.onCreateView = onCreateView
    }

    fun onBind(onBindView: B.(position: Int, item: T) -> Unit) {
        this.onBindView = onBindView
    }

    fun onBindPayloads(onBindPayloads: B.(position: Int, item: T, payloads: MutableList<Any>) -> Unit) {
        this.onBindPayloads = onBindPayloads
    }

    fun onDetached(onDetached: B.() -> Unit) {
        this.onDetached = onDetached
    }

    fun onAttached(onAttached: B.() -> Unit) {
        this.onAttached = onAttached
    }

    internal fun onCreateViewHolder(parent: ViewGroup) = BindingViewHolder(createBinding(LayoutInflater.from(parent.context), parent, false).apply(onCreateView))

    internal fun onBindViewHolder(viewHolder: BindingViewHolder<B>, item: T, position: Int) = viewHolder.binding.onBindView(position, item)

    internal fun onBindViewHolder(viewHolder: BindingViewHolder<B>, item: T, position: Int, payloads: MutableList<Any>) =
        viewHolder.binding.onBindPayloads(position, item, payloads)

    internal fun onDetachViewHolder(viewHolder: BindingViewHolder<B>) = viewHolder.binding.onDetached()
    internal fun onAttachViewHolder(viewHolder: BindingViewHolder<B>) = viewHolder.binding.onAttached()
}
