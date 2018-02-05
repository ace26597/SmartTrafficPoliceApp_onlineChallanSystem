package github.nisrulz.projectqreader;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.TextView;

import java.util.List;


public class SubscribeAdapter extends RecyclerView.Adapter<SubscribeAdapter.DataObjectHolder>
{
    private List<Subscribe_pogos_list> SubscribeList;
    Context context;



    MyClickListener myClickListener;


    public SubscribeAdapter(Context context, List<Subscribe_pogos_list> subscribe_pogos_list)
    {
        SubscribeList=subscribe_pogos_list;
        context=context;

    }

    public interface MyClickListener {
    }


    public static class DataObjectHolder extends RecyclerView.ViewHolder implements View.OnClickListener
    {
        Context co;

        TextView User_id;
        TextView Feed_Data;




        public DataObjectHolder(View itemView, Context c)
        {
            super(itemView);
            this.co=c;
            User_id=(TextView)itemView.findViewById(R.id.SubscribeName);
            Feed_Data=(TextView)itemView.findViewById(R.id.Subscribe_id);

        }

        @Override
        public void onClick(View v) {

        }
    }

    @Override
    public DataObjectHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_feeddetail,parent, false);

        DataObjectHolder holder = new DataObjectHolder(v, parent.getContext());

        return holder;
    }

    @Override
    public void onBindViewHolder(DataObjectHolder holder, final int position) {

        holder.User_id.setText(SubscribeList.get(position).getUser_id());

        holder.Feed_Data.setText(SubscribeList.get(position).getFeedback_data());

    }


    @Override
    public int getItemCount() {
        return SubscribeList.size();
    }



}
