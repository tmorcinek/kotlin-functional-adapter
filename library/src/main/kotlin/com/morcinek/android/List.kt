package com.morcinek.android

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.recyclerview.widget.*


fun <T> RecyclerView.list(diffCallback: DiffUtil.ItemCallback<T>, body: FListAdapter<T>.() -> Unit) {
    layoutManager = LinearLayoutManager(context)
    adapter = FListAdapter(diffCallback).apply(body)
}

fun <T> listAdapter(diffCallback: DiffUtil.ItemCallback<T>, body: FListAdapter<T>.() -> Unit) = FListAdapter(diffCallback).apply(body)

class FListAdapter<T>(diffCallback: DiffUtil.ItemCallback<T>) : FAdapter<T>() {

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
