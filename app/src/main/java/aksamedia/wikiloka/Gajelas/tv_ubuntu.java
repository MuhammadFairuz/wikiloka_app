package aksamedia.wikiloka.Gajelas;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

public class tv_ubuntu extends TextView {

    public tv_ubuntu(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        setType(context);
    }

    public tv_ubuntu(Context context, AttributeSet attrs) {
        super(context, attrs);
        setType(context);
    }

    public tv_ubuntu(Context context) {
        super(context);
        setType(context);
    }

    private void setType(Context context){
        this.setTypeface(Typeface.createFromAsset(context.getAssets(),
                "font_ubuntu.ttf"));
    }
}