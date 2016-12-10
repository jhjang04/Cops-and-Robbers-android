package car.adroid.util;

import android.app.ProgressDialog;
import android.content.Context;

/**
 * Created by jjh on 2016-12-10.
 */

public class ProgressThread extends Thread {

    Context mContext;
    ProgressDialog mProgressDlg = null;

    public ProgressThread(Context context){
        mContext = context;
    }

    @Override
    public synchronized void start() {
        mProgressDlg  = ProgressDialog.show(mContext,"", "wait...." );
        super.start();
        new Thread() {
            @Override
            public void run() {
                super.run();
                try {
                    ProgressThread.this.join();
                    if (mProgressDlg != null && mProgressDlg.isShowing()) {
                        mProgressDlg.dismiss();
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }
}
