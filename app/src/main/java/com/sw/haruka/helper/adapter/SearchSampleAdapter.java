//package com.sw.haruka.model.adapter;
//
//import android.content.Context;
//import android.os.Handler;
//import android.os.Message;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.view.animation.AnimationUtils;
//import android.widget.Button;
//
//import androidx.annotation.NonNull;
//import androidx.recyclerview.widget.RecyclerView;
//
//import com.sw.haruka.R;
//import com.sw.haruka.model.entity.SearchSample;
//import com.sw.haruka.utility.HarukaHandlerConstant;
//
//import java.util.List;
//
//public class SearchSampleAdapter extends RecyclerView.Adapter<SearchSampleAdapter.SampleViewHolder> {
//
//    private Context mContext;
//    private List<SearchSample> mSampleList;
//    private Handler mHandler;
//    public SearchSampleAdapter(Context context, Handler handler, List<SearchSample> sampleList) {
//        mContext = context;
//        mHandler = handler;
//        mSampleList = sampleList;
//    }
//
//    @NonNull
//    @Override
//    public SampleViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//        return new SampleViewHolder(LayoutInflater.from(mContext).inflate(R.layout.item_sample_search, parent, false));
//    }
//
//    @Override
//    public void onBindViewHolder(@NonNull SampleViewHolder holder, final int position) {
//        SearchSample sample = mSampleList.get(position);
//        holder.button.setText(sample.getName());
//        holder.itemView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Message message = new Message();
//                message.what = HarukaHandlerConstant.OPEN_SAMPLE_FOLDER;
//                message.obj = position;
//                mHandler.sendMessage(message);
//            }
//        });
//        holder.itemView.startAnimation(AnimationUtils.loadAnimation(mContext, R.anim.dialog_enter_bottom));
//    }
//
//    @Override
//    public int getItemCount() {
//        return mSampleList.size();
//    }
//
//    class SampleViewHolder extends RecyclerView.ViewHolder{
//        private Button button;
//        public SampleViewHolder(@NonNull View itemView) {
//            super(itemView);
//            button = itemView.findViewById(R.id.button_sample);
//        }
//    }
//}
