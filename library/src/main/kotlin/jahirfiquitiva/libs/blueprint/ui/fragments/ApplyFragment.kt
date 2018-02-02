/*
 * Copyright (c) 2018. Jahir Fiquitiva
 *
 * Licensed under the CreativeCommons Attribution-ShareAlike
 * 4.0 International License. You may not use this file except in compliance
 * with the License. You may obtain a copy of the License at
 *
 *    http://creativecommons.org/licenses/by-sa/4.0/legalcode
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package jahirfiquitiva.libs.blueprint.ui.fragments

import android.support.v7.widget.GridLayoutManager
import android.view.View
import ca.allanwang.kau.utils.isAppInstalled
import com.pluscubed.recyclerfastscroll.RecyclerFastScroller
import jahirfiquitiva.libs.blueprint.R
import jahirfiquitiva.libs.blueprint.data.models.Launcher
import jahirfiquitiva.libs.blueprint.helpers.extensions.executeLauncherIntent
import jahirfiquitiva.libs.blueprint.helpers.extensions.showLauncherNotInstalledDialog
import jahirfiquitiva.libs.blueprint.helpers.extensions.supportedLaunchers
import jahirfiquitiva.libs.blueprint.ui.adapters.LaunchersAdapter
import jahirfiquitiva.libs.frames.ui.widgets.EmptyViewRecyclerView
import jahirfiquitiva.libs.kauextensions.extensions.bind
import jahirfiquitiva.libs.kauextensions.extensions.ctxt
import jahirfiquitiva.libs.kauextensions.extensions.getDimensionPixelSize
import jahirfiquitiva.libs.kauextensions.extensions.getInteger
import jahirfiquitiva.libs.kauextensions.extensions.hasContent
import jahirfiquitiva.libs.kauextensions.ui.decorations.GridSpacingItemDecoration
import jahirfiquitiva.libs.kauextensions.ui.fragments.Fragment

@Suppress("DEPRECATION")
class ApplyFragment : Fragment<Launcher>() {
    override fun getContentLayout(): Int = R.layout.section_layout
    
    private val list = ArrayList<Launcher>()
    private lateinit var adapter: LaunchersAdapter
    
    override fun initUI(content: View) {
        val rv: EmptyViewRecyclerView by content.bind(R.id.list_rv)
        val fastScroller: RecyclerFastScroller by content.bind(R.id.fast_scroller)
        rv.emptyView = content.findViewById(R.id.empty_view)
        rv.textView = content.findViewById(R.id.empty_text)
        adapter = LaunchersAdapter { onItemClicked(it, false) }
        rv.adapter = adapter
        val columns = ctxt.getInteger(R.integer.icons_columns) - 1
        rv.layoutManager = GridLayoutManager(context, columns, GridLayoutManager.VERTICAL, false)
        rv.addItemDecoration(
                GridSpacingItemDecoration(
                        columns, ctxt.getDimensionPixelSize(R.dimen.cards_margin)))
        rv.state = EmptyViewRecyclerView.State.LOADING
        fastScroller.attachRecyclerView(rv)
        ctxt.supportedLaunchers.forEach {
            list.add(it)
        }
        setAdapterItems(list)
        rv.state = EmptyViewRecyclerView.State.NORMAL
    }
    
    fun applyFilter(filter: String = "") {
        if (filter.hasContent()) {
            setAdapterItems(ArrayList(list.filter { it.name.contains(filter, true) }))
        } else {
            setAdapterItems(list)
        }
    }
    
    private fun setAdapterItems(items: ArrayList<Launcher>) {
        adapter.setItems(
                ArrayList(items.distinct().sortedBy { !isLauncherInstalled(it.packageNames) }))
    }
    
    private fun isLauncherInstalled(packages: Array<String>): Boolean {
        packages.forEach {
            if (context?.isAppInstalled(it) == true) return true
        }
        return false
    }
    
    override fun onItemClicked(item: Launcher, longClick: Boolean) {
        if (!longClick) {
            if (isLauncherInstalled(item.packageNames) || item.name.contains("lineage", true))
                context?.executeLauncherIntent(item.name)
            else context?.showLauncherNotInstalledDialog(item)
        }
    }
}