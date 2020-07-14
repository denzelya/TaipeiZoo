package com.den.taipeizoo.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.den.taipeizoo.R
import com.den.taipeizoo.models.District
import com.den.taipeizoo.network.TaipeiZooApi
import com.den.taipeizoo.network.TaipeiZooRepository
import kotlinx.android.synthetic.main.activity_taipei_zoo.*
import kotlinx.android.synthetic.main.item_district.view.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch


class TaipeiZooActivity : AppCompatActivity() {

    private var homeTitle = ""
    private var currentDistrict = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_taipei_zoo)

        setSupportActionBar(taipei_zoo_toolbar)
        taipei_zoo_toolbar.title = title
        homeTitle = title.toString()

        val repository = TaipeiZooRepository(TaipeiZooApi())
        GlobalScope.launch(Dispatchers.Main) {
            val district = repository.getDistrict()
            setupRecyclerView(taipei_zoo_district_list, district.result.results)
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                onBackPressed()

                val count = this.supportFragmentManager.backStackEntryCount
                if (count == 0) {
                    taipei_zoo_toolbar.title = homeTitle
                    supportActionBar?.setDisplayHomeAsUpEnabled(false)
                } else if (count == 1) {
                    taipei_zoo_toolbar.title = currentDistrict
                }
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun setupRecyclerView(recyclerView: RecyclerView, districts: List<District>) {
        recyclerView.adapter =
            DistrictRecyclerViewAdapter(
                this,
                districts
            )

        recyclerView.addItemDecoration(
            DividerItemDecoration(
                this,
                LinearLayoutManager.VERTICAL
            )
        )
    }

    class DistrictRecyclerViewAdapter(
        private val parentActivity: TaipeiZooActivity,
        private val values: List<District>
    ) :
        RecyclerView.Adapter<DistrictRecyclerViewAdapter.ViewHolder>() {

        private val onClickListener: View.OnClickListener

        init {
            onClickListener = View.OnClickListener { v ->

                val district = v.tag as District

                parentActivity.apply {
                    supportActionBar?.setDisplayHomeAsUpEnabled(true)
                    parentActivity.title = district.E_Name
                    currentDistrict = district.E_Name
                }


                val fragment = DistrictDetailFragment()
                    .apply {
                        arguments = Bundle().apply {
                            putString(DistrictDetailFragment.ARG_DISTRICT_NAME, district.E_Name)
                            putString(DistrictDetailFragment.ARG_DISTRICT_CONTENT, district.E_Info)
                            putString(
                                DistrictDetailFragment.ARG_DISTRICT_PIC_URL,
                                district.E_Pic_URL
                            )
                            putString(
                                DistrictDetailFragment.ARG_DISTRICT_MEMO,
                                getMemo(district.E_Memo)
                            )
                            putString(DistrictDetailFragment.ARG_DISTRICT_URL, district.E_URL)
                            putString(
                                DistrictDetailFragment.ARG_DISTRICT_CATEGORY,
                                district.E_Category
                            )
                        }
                    }
                parentActivity.supportFragmentManager.beginTransaction()
                    .add(parentActivity.taipei_zoo_frameLayout.id, fragment)
                    .addToBackStack(district.E_Name).commit()
            }
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_district, parent, false)
            return ViewHolder(view)
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            val item = values[position]
            holder.nameTv.text = item.E_Name
            holder.infoTv.text = item.E_Info
            holder.picIv.loadFromUrl(item.E_Pic_URL)
            holder.memoTv.text = getMemo(item.E_Memo)

            with(holder.itemView) {
                tag = item
                setOnClickListener(onClickListener)
            }
        }

        override fun getItemCount() = values.size

        inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
            val nameTv: TextView = view.district_name
            val infoTv: TextView = view.district_info
            val picIv: ImageView = view.district_pic
            val memoTv: TextView = view.district_memo
        }

        private fun getMemo(memo: String): String {
            return if (memo.isEmpty()) {
                "無休館資訊"
            } else {
                memo
            }
        }
    }
}

fun ImageView.loadFromUrl(imageUrl: String) {
    // TODO add default image
    Glide.with(this).load(imageUrl).into(this)
}
