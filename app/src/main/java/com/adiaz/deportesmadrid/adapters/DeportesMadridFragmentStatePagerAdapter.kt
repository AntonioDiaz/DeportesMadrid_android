package com.adiaz.deportesmadrid.adapters

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentStatePagerAdapter

import java.util.ArrayList

class DeportesMadridFragmentStatePagerAdapter(fm: FragmentManager) : FragmentStatePagerAdapter(fm) {

    private val mFragmentList: MutableList<Fragment>
    private val mFragmentTitleList: MutableList<String>

    init {
        mFragmentList = ArrayList()
        mFragmentTitleList = ArrayList()
    }

    override fun getItem(position: Int): Fragment {
        return mFragmentList[position]
    }

    override fun getCount(): Int {
        return mFragmentList.size
    }

    fun addFragment(fragment: Fragment, title: String) {
        mFragmentList.add(fragment)
        mFragmentTitleList.add(title)
    }

    override fun getPageTitle(position: Int): CharSequence {
        return mFragmentTitleList[position]
    }

    override fun getItemPosition(`object`: Any?): Int {
        return super.getItemPosition(`object`)
    }
}
