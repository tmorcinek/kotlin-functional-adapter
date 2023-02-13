package com.morcinek.android

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.recyclerview.widget.*
import androidx.viewbinding.ViewBinding

fun <T, B : ViewBinding> RecyclerView.list(diffCallback: DiffUtil.ItemCallback<T>, createBinding: (LayoutInflater, ViewGroup?, Boolean) -> B, body: BListAdapter<T, B>.() -> Unit) {
    layoutManager = LinearLayoutManager(context)
    adapter = listAdapter(diffCallback, createBinding, body)
}

fun <T, B : ViewBinding> listAdapter(diffCallback: DiffUtil.ItemCallback<T>, createBinding: (LayoutInflater, ViewGroup?, Boolean) -> B, body: BListAdapter<T, B>.() -> Unit) =
    BListAdapter(diffCallback, createBinding).apply(body)

class BListAdapter<T, B : ViewBinding>(diffCallback: DiffUtil.ItemCallback<T>, createBinding: (LayoutInflater, ViewGroup?, Boolean) -> B) : BAdapter<T, B>(createBinding) {

    private val mDiffer: AsyncListDiffer<T> = AsyncListDiffer(
        AdapterListUpdateCallback(this),
        AsyncDifferConfig.Builder(diffCallback).build()
    )

    fun submitList(list: List<T>, commitCallback: Runnable? = null) = mDiffer.submitList(list, commitCallback)

    override fun getItem(position: Int): T = mDiffer.currentList[position]

    override fun getItemCount() = mDiffer.currentList.size

    fun liveData(lifecycleOwner: LifecycleOwner, liveData: LiveData<List<T>>, commitCallback: ((List<T>) -> Unit)? = null) =
        liveData.observe(lifecycleOwner) { submitList(it) { commitCallback?.invoke(currentList) } }

    val currentList: List<T>
        get() = mDiffer.currentList
}

fun <T, B : ViewBinding> staticAdapter(createBinding: (LayoutInflater, ViewGroup?, Boolean) -> B, items: List<T>, body: BAdapter<T, B>.() -> Unit) =
    StaticAdapter(createBinding, items).apply(body)

class StaticAdapter<T, B : ViewBinding>(createBinding: (LayoutInflater, ViewGroup?, Boolean) -> B, private val items: List<T>) : BAdapter<T, B>(createBinding) {

    override fun getItem(position: Int) = items[position]

    override fun getItemCount() = items.size
}
