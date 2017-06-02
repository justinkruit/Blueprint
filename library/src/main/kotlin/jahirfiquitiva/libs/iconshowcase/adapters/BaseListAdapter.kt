/*
 * Copyright (c) 2017.  Jahir Fiquitiva
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
 *
 * Special thanks to the project contributors and collaborators
 *   https://github.com/jahirfiquitiva/IconShowcase#special-thanks
 */

package jahirfiquitiva.libs.iconshowcase.adapters

import android.support.v7.widget.RecyclerView
import jahirfiquitiva.libs.iconshowcase.adapters.presenters.ItemsAdapterPresenter

abstract class BaseListAdapter<T>:RecyclerView.Adapter<RecyclerView.ViewHolder>(), ItemsAdapterPresenter<T> {

    val list = ArrayList<T>()

    override fun clearList() {
        val size = itemCount
        list.clear()
        notifyItemRangeRemoved(0, size)
    }

    override fun addAll(items:ArrayList<T>) {
        val prevSize = itemCount
        list.addAll(items)
        notifyItemRangeInserted(prevSize, items.size)
    }
}