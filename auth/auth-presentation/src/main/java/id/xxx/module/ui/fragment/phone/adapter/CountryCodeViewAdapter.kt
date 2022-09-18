package id.xxx.module.ui.fragment.phone.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import id.xxx.module.auth.presentation.R
import id.xxx.module.data.model.CountryCode

class CountryCodeViewAdapter(
    private val items: List<CountryCode>,
    private val onItemClick: (pos: Int, data: CountryCode) -> Unit = { _, _ -> }
) : RecyclerView.Adapter<CountryCodeViewAdapter.ViewHolder>() {

    class ViewHolder(val view: View) : RecyclerView.ViewHolder(view)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.country_code_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]
        val view = holder.view
        view.findViewById<TextView>(R.id.tv_flag).text = item.flag
        view.findViewById<TextView>(R.id.tv_name).text = item.name
        val tvDialCode = view.findViewById<TextView>(R.id.tv_dial_code)
        val dialCode = item.dial_code
        tvDialCode.text = tvDialCode.text.replaceRange(0, dialCode.length, dialCode)
        view.setOnClickListener {
            onItemClick.invoke(position, item)
        }
    }

    override fun getItemCount() =
        items.size
}