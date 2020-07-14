package com.den.taipeizoo.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.den.taipeizoo.R
import kotlinx.android.synthetic.main.fragment_plant_detail.view.*


class PlantDetailFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView =
            LayoutInflater.from(context).inflate(R.layout.fragment_plant_detail, container, false)

        arguments?.let {
            rootView.fragment_plant_name_ch.text = it.getString(ARG_PLANT_NAME_CH)
            rootView.fragment_plant_name_en.text = it.getString(ARG_PLANT_NAME_EN)
            it.getString(ARG_PLANT_PIC_URL)?.apply {
                rootView.fragment_plant_pic.loadFromUrl(this)
            }
            rootView.fragment_plant_also_name.text = it.getString(ARG_PLANT_ALSO_KNOWN)
            rootView.fragment_plant_function.text = it.getString(ARG_PLANT_FUNCTION)
            rootView.fragment_plant_brief.text = it.getString(ARG_PLANT_BRIEF)
            rootView.fragment_plant_feature.text = it.getString(ARG_PLANT_DIFFER)
            rootView.fragment_plant_update_time.text = it.getString(ARG_PLANT_UPDATE_TIME)
        }

        return rootView
    }

    companion object {
        const val ARG_PLANT_NAME_CH = "plant_name_ch"
        const val ARG_PLANT_NAME_EN = "plant_name_en"
        const val ARG_PLANT_PIC_URL = "plant_pic_url"
        const val ARG_PLANT_ALSO_KNOWN = "plant_also_known"
        const val ARG_PLANT_FUNCTION = "plant_function"
        const val ARG_PLANT_BRIEF = "plant_brief"
        const val ARG_PLANT_DIFFER = "plant_differ"
        const val ARG_PLANT_UPDATE_TIME = "plant_update_time"
    }
}