package com.den.taipeizoo.view

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.den.taipeizoo.R
import com.den.taipeizoo.models.Plant
import com.den.taipeizoo.network.TaipeiZooApi
import com.den.taipeizoo.network.TaipeiZooRepository
import kotlinx.android.synthetic.main.activity_taipei_zoo.*
import kotlinx.android.synthetic.main.fragment_district_detail.*
import kotlinx.android.synthetic.main.fragment_district_detail.view.*
import kotlinx.android.synthetic.main.item_district.view.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class DistrictDetailFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView =
            LayoutInflater.from(context)
                .inflate(R.layout.fragment_district_detail, container, false)

        arguments?.let {
            rootView.fragment_district_content.text = it.getString(ARG_DISTRICT_CONTENT)
            rootView.fragment_district_memo.text = it.getString(ARG_DISTRICT_MEMO)
            it.getString(ARG_DISTRICT_PIC_URL)?.apply {
                rootView.fragment_district_pic.loadFromUrl(this)
            }
            rootView.fragment_district_category.text = it.getString(ARG_DISTRICT_CATEGORY)

            val url = it.getString(ARG_DISTRICT_URL)
            rootView.fragment_district_web.setOnClickListener {
                val uri: Uri = Uri.parse(url)
                val intent = Intent(Intent.ACTION_VIEW, uri)
                startActivity(intent)
            }

            val name = it.getString(ARG_DISTRICT_NAME)
            val repository = TaipeiZooRepository(TaipeiZooApi())
            GlobalScope.launch(Dispatchers.Main) {
                val plants = repository.getPlant(location = name)
                setupRecyclerView(recycler_view_plant, plants.result.results)
            }
        }

        return rootView
    }

    private fun setupRecyclerView(recyclerView: RecyclerView, plants: List<Plant>) {
        recyclerView.layoutManager = LinearLayoutManager(this.context)
        recyclerView.adapter = PlantRecyclerViewAdapter(
            this.activity as TaipeiZooActivity,
            plants
        )

        recyclerView.addItemDecoration(
            DividerItemDecoration(
                this.context,
                LinearLayoutManager.VERTICAL
            )
        )
    }

    class PlantRecyclerViewAdapter(
        private val parentActivity: TaipeiZooActivity,
        private val values: List<Plant>
    ) :
        RecyclerView.Adapter<PlantRecyclerViewAdapter.ViewHolder>() {

        private val onClickListener: View.OnClickListener

        init {
            onClickListener = View.OnClickListener { v ->

                val plant = v.tag as Plant
                val name = getFirstName(plant.F_Name_Ch, plant.F_AlsoKnown)

                parentActivity.apply {
                    parentActivity.title = name
                }

                val fragment = PlantDetailFragment()
                    .apply {
                        arguments = Bundle().apply {
                            putString(PlantDetailFragment.ARG_PLANT_NAME_CH, name)
                            putString(PlantDetailFragment.ARG_PLANT_NAME_EN, plant.F_Name_En)
                            putString(PlantDetailFragment.ARG_PLANT_PIC_URL, plant.F_Pic01_URL)
                            putString(PlantDetailFragment.ARG_PLANT_ALSO_KNOWN, plant.F_AlsoKnown)
                            putString(PlantDetailFragment.ARG_PLANT_BRIEF, plant.F_Brief)
                            putString(PlantDetailFragment.ARG_PLANT_DIFFER, plant.F_Feature)
                            putString(PlantDetailFragment.ARG_PLANT_FUNCTION, plant.F_Function)
                            putString(PlantDetailFragment.ARG_PLANT_UPDATE_TIME, plant.F_Update)
                        }
                    }
                parentActivity.supportFragmentManager.beginTransaction()
                    .add(parentActivity.taipei_zoo_frameLayout.id, fragment)
                    .addToBackStack("plant_detail").commit()
            }
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_district, parent, false)
            return ViewHolder(view)
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            val item = values[position]

            // TODO F_Name_Ch parser 有問題
            // holder.nameTv.text = item.F_Name_Ch

            holder.nameTv.text = getFirstName(item.F_Name_Ch, item.F_AlsoKnown)
            holder.infoTv.text = item.F_AlsoKnown
            holder.picIv.loadFromUrl(item.F_Pic01_URL)
            holder.memoTv.visibility = View.GONE
            with(holder.itemView) {
                tag = item
                setOnClickListener(onClickListener)
            }
        }

        private fun getFirstName(chName: String, names: String): String {
            return if (chName.isNullOrEmpty()) {
                names.substringBefore("、")
            } else {
                chName
            }
        }

        override fun getItemCount() = values.size

        inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
            val nameTv: TextView = view.district_name
            val infoTv: TextView = view.district_info
            val picIv: ImageView = view.district_pic
            val memoTv: TextView = view.district_memo
        }
    }

    companion object {
        const val ARG_DISTRICT_NAME = "district_name"
        const val ARG_DISTRICT_PIC_URL = "district_pic_url"
        const val ARG_DISTRICT_CONTENT = "district_content"
        const val ARG_DISTRICT_MEMO = "district_memo"
        const val ARG_DISTRICT_URL = "district_url"
        const val ARG_DISTRICT_CATEGORY = "district_category"
    }
}