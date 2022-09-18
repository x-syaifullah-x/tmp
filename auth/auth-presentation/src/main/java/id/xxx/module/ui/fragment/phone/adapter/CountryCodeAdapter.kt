package id.xxx.module.ui.fragment.phone.adapter

import android.content.Context
import android.view.*
import android.widget.ArrayAdapter
import android.widget.PopupWindow
import android.widget.Spinner
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import id.xxx.module.auth.presentation.R
import id.xxx.module.data.model.CountryCode

class CountryCodeAdapter(
    context: Context,
    private val items: List<CountryCode>
) : ArrayAdapter<CountryCode>(context, 0, items) {

    private val layoutInflater: LayoutInflater = LayoutInflater.from(context)

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view = convertView
            ?: layoutInflater.inflate(R.layout.country_code_item, parent, false)
        val item = getItem(position)
        if (item != null) {
            view.findViewById<TextView>(R.id.tv_flag).text = item.flag
            view.findViewById<TextView>(R.id.tv_name).text = item.name
            view.findViewById<TextView>(R.id.tv_dial_code).text = item.dial_code
        }
        view.setOnClickListener {
            showPopUpDropDown(parent, view)
        }
        return view
    }

    private fun showPopUpDropDown(parent: ViewGroup?, child: View) {
        val contentView = layoutInflater.inflate(
            R.layout.country_code_dropdown, child as? ViewGroup, false
        )
        val popupWindow =
            PopupWindow(child.width, WindowManager.LayoutParams.WRAP_CONTENT)
        val header = contentView.findViewById<TextView>(R.id.tv_header)
        header.setOnClickListener {
            popupWindow.dismiss()
        }
        val rv = contentView.findViewById<RecyclerView>(R.id.rv_country_code)
        popupWindow.contentView = contentView
        popupWindow.isFocusable = true
        popupWindow.isOutsideTouchable = true
        popupWindow.isTouchable = true
        popupWindow.showAsDropDown(child, 0, 0 - child.height, Gravity.NO_GRAVITY)
        rv.adapter = CountryCodeViewAdapter(
            items,
            onItemClick = { pos, _ ->
                (parent as? Spinner)?.setSelection(pos)
                popupWindow.dismiss()
            }
        )
    }
}