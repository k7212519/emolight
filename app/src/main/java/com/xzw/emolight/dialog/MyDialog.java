package com.xzw.emolight.dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.xzw.emolight.R;

public class MyDialog extends Dialog {

    private ProgressBar progressBar;
    private TextView textProgress, textViewMsg;
//    private Context context;

    public MyDialog(Context context) {
        super(context, R.style.transparent_dialog);
//        this.context= context;
        createLoadingDialog(context);
    }

    private void createLoadingDialog(final Context context) {
        //从context获取当前布局
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        //得到view
        View view = layoutInflater.inflate(R.layout.dialog_progross, null);
        //加载布局
        LinearLayout linearLayout = view.findViewById(R.id.dialog_view);
        progressBar = view.findViewById(R.id.pb_Circle);
        textViewMsg = view.findViewById(R.id.tv_msg);
        textProgress = view.findViewById(R.id.tv_progress);

        //设置不可通过点击外面区域取消
        setCanceledOnTouchOutside(false);

        //设置布局为全屏
        setContentView(linearLayout, new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT));

        //取消进度监听器
        setOnCancelListener(new OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                Toast.makeText(context,"加载取消",Toast.LENGTH_SHORT).show();
            }
        });
    }

    // 设置加载信息
    public void setMessage(String msg){
        textViewMsg.setText(msg);
    }

    //设置进度条
    public void setProgressBar(ProgressBar progressBar) {
        this.progressBar = progressBar;
    }

    //获取进度条
    public ProgressBar getProgressBar() {
        return progressBar;
    }

    //设置进度
    public void setProgress(int progress){
        textProgress.setText(progress*100/progressBar.getMax() + "%");
        progressBar.setProgress(progress);
    }

}
