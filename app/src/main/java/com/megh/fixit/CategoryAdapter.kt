import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.megh.fixit.R

class CategoryAdapter(
    private val list: List<Category>,
    private val context: Context
) : RecyclerView.Adapter<CategoryAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val title: TextView = view.findViewById(R.id.txtTitle)
        val subtitle: TextView = view.findViewById(R.id.txtSubtitle)
        val icon: ImageView = view.findViewById(R.id.imgIcon)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_category, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = list[position]

        holder.title.text = item.name ?: ""
        holder.subtitle.text = (item.subtitle ?: "").trim().trim('"') // removes quotes if stored like "text"

        // icon from Firestore -> must match drawable name exactly
        val iconName = (item.icon ?: "").trim().trim('"')
        val resId = holder.itemView.context.resources.getIdentifier(
            iconName,
            "drawable",
            holder.itemView.context.packageName
        )
        holder.icon.setImageResource(if (resId != 0) resId else R.drawable.ic_launcher_foreground)
    }



    override fun getItemCount(): Int = list.size
}
