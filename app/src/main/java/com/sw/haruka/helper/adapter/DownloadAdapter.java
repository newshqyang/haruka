package com.sw.haruka.helper.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.sw.haruka.R;
import com.sw.haruka.model.DownloadThread;
import com.sw.haruka.model.entity.FileShareTask;
import com.sw.haruka.helper.utils.FileUtils;


public class DownloadAdapter extends RecyclerView.Adapter<DownloadAdapter.DownloadViewHolder> {

    private Context mContext;
    private DownloadThread mDownloadThread;
    public DownloadAdapter(Context context, DownloadThread thread) {
        mContext = context;
        mDownloadThread = thread;
    }

    @NonNull
    @Override
    public DownloadViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new DownloadViewHolder(LayoutInflater.from(mContext).inflate(R.layout.layout_download_card, parent, false));
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull DownloadViewHolder holder, final int position) {
        if (mDownloadThread.getFileReceiveTaskList().size() == 0) {
            return;    // 空的时候，不要显示了
        }
        FileShareTask task = mDownloadThread.getFileReceiveTaskList().get(position);
        if (task.getState() == FileShareTask.STATE_CANCEL || task.getState() == FileShareTask.STATE_DONE) {
            holder.itemView.setVisibility(View.GONE);
            return;    // 取消的、下载完成的，不要显示了
        }
        holder.fileName.setText(task.getFileName());   // 文件名
        if (task.getFileName().contains(".")) { // 设置文件类型图片
            ImageView type = holder.itemView.findViewById(R.id.imageView_fileType);
            type.setImageDrawable(mContext.getDrawable(FileUtils.getTypeImage(FileUtils.getSuffix(task.getFileName()))));
        }
        holder.cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mDownloadThread.cancelTask(position);
            }
        });
        if (task.getState() == FileShareTask.STATE_RUNNING) {   // 如果正在传输
            holder.percent.setText(task.getProgress() + "%");// 进度，圆角按钮
            holder.speed.setText(FileUtils.getFormatSize(task.getSpeed()) + "/s");// 传输速度
            holder.sharedSize.setText(FileUtils.getFormatSize(task.getSharedSize())
                    + "/" + FileUtils.getFormatSize(task.getFileSize()));   // 已发大小
            holder.progressBar.setProgress(task.getProgress());    // 传输进度
        } else {        // 如果还没开始，就显示等待
            holder.percent.setText("等待");
            holder.speed.setVisibility(View.INVISIBLE);
            holder.sharedSize.setVisibility(View.INVISIBLE);
            holder.progressBar.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public int getItemCount() {
        return mDownloadThread.getFileReceiveTaskList().size();
    }


    class DownloadViewHolder extends RecyclerView.ViewHolder{
        TextView fileName;
        TextView speed;
        TextView sharedSize;
        ProgressBar progressBar;
        ImageButton cancel;
        Button percent;
        public DownloadViewHolder(@NonNull View itemView) {
            super(itemView);
            fileName = itemView.findViewById(R.id.textView_fileName);
            speed = itemView.findViewById(R.id.textView_speed);// 传输速度
            sharedSize = itemView.findViewById(R.id.textView_process);// 已发大小
            progressBar = itemView.findViewById(R.id.progressBar_progress);// 传输进度
            cancel = itemView.findViewById(R.id.imageButton_cancel);   // 取消
            percent = itemView.findViewById(R.id.button_process);   // 进度，圆角按钮
        }
    }
}
