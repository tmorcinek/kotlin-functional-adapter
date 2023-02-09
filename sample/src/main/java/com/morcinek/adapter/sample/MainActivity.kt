package com.morcinek.adapter.sample

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.tabs.TabLayoutMediator
import com.morcinek.android.HasKey
import com.morcinek.android.customAdapter
import com.morcinek.android.itemCallback
import com.morcinek.android.list
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.vh_city.view.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        viewPager.run {
            adapter = customAdapter {
                itemView<RecyclerView>(R.layout.recycler_view) {
                    list<City>(itemCallback()) {
                        resId(R.layout.vh_city)
                        onBind { position, item ->
                            number.text = "${position + 1}."
                            name.text = item.name
                        }
                        submitList(cities)
                    }
                }
            }

        }
        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            tab.text = when (position) {
                0 -> "Cities"
                else -> "Other"
            }
        }.attach()
    }


    private val cities = listOf(
        City("Barcelona"),
        City("Warsaw"),
        City("Krakow"),
        City("Madrid"),
        City("Lisbon"),
        City("Porto"),
        City("Hamburg"),
        City("London"),
        City("Liverpool"),
    )
}


private class City(val name: String) : HasKey {
    override val key: String get() = name
}