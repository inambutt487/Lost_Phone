package find.com.lostphone;

import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDex;

/**
 * Created by Aurang Zeb on 07-Aug-17.
 */

public class MyApplication  extends Application{
    private static final MyApplication ourInstance = new MyApplication();

    static MyApplication getInstance() {
        return ourInstance;
    }

    public MyApplication() {
    }
    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }
}
