@file:Suppress("DEPRECATION")

package universal.appfactory.CarbonDioxideViewer.adapters

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import universal.appfactory.CarbonDioxideViewer.adapters.LayoutFragment1
import universal.appfactory.CarbonDioxideViewer.adapters.LayoutFragment2
import universal.appfactory.CarbonDioxideViewer.adapters.LayoutFragment3

class PageAdapter(fm: FragmentManager, pageCount: Int=3): FragmentPagerAdapter(fm) {
    private val pages: Int = pageCount

    override fun getCount(): Int{
        return pages
    }

    override fun getPageTitle(position: Int): CharSequence {
        return when(position){
            0 -> "Dashboard"
            1 -> "Map"
            2 -> "More Info"
            else -> "N/A"
        }
    }

    override fun getItem(position: Int): Fragment {
        val bundle = Bundle()
        val fragment: Fragment = when(position){
            0 -> {
                bundle.putInt("pageNo", 1)
                LayoutFragment1()
            }
            1 -> {
                bundle.putInt("pageNo", 2)
                LayoutFragment2()
            }
            2 -> {
                bundle.putInt("pageNo", 3)
                LayoutFragment3()
            }
            else -> {
                bundle.putInt("pageNo", 1)
                LayoutFragment1()
            }
        }

        fragment.arguments = bundle
        return fragment
    }

}
