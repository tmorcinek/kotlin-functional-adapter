package com.morcinek.adapter.sample

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.tabs.TabLayoutMediator
import com.morcinek.android.*
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.vh_city.view.*
import kotlinx.android.synthetic.main.vh_city.view.name
import kotlinx.android.synthetic.main.vh_name.view.*

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
                itemView<RecyclerView>(R.layout.recycler_view) {
                    setup {
                        grid(2) { setupSpanSizeLookup { position -> if (position % 3 == 0) 2 else 1 } }
                        adapter(listAdapter<City>(itemCallback()) {
                            resId(R.layout.vh_city)
                            onBind { position, item ->
                                number.text = "${position + 1}."
                                name.text = item.name
                            }
                            submitList(cities)
                        })
                    }
                }
                itemView<RecyclerView>(R.layout.recycler_view) {
                    list<String>(itemCallback { areItemsTheSame { s, s2 -> s == s2 } }){
                        resId(R.layout.vh_name)
                        onBind { _, item ->
                            name.text = item
                        }
                        submitList(names)
                    }
                }
            }

        }
        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            tab.text = when (position) {
                0 -> "Cities List"
                1 -> "Cities Grid"
                2 -> "Names"
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
        City("Manchester"),
        City("Paris"),
        City("Milan"),
        City("Turin"),
        City("Moscow"),
        City("Moscow"),
    )

    private val names = listOf("Tomek", "Basia", "Kamil", "Krzysiu", "Karolina", "Beata", "Marek", "Grzegorz", "Mikolaj", "Wiktor")
}


private class City(val name: String) : HasKey {
    override val key: String get() = name
}