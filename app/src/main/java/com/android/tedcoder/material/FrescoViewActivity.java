package com.android.tedcoder.material;

import android.content.Intent;
import android.graphics.drawable.Animatable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.facebook.common.logging.FLog;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.controller.BaseControllerListener;
import com.facebook.drawee.controller.ControllerListener;
import com.facebook.drawee.drawable.ProgressBarDrawable;
import com.facebook.drawee.generic.GenericDraweeHierarchy;
import com.facebook.drawee.generic.GenericDraweeHierarchyBuilder;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.image.ImageInfo;
import com.facebook.imagepipeline.image.QualityInfo;

/**
 * 图片的加载 facebook
 * <p/>
 * 在 Application 初始化时，在应用调用 setContentView() 之前，进行初始化;
 * Created by kjh08490 on 2016/3/18.
 */
public class FrescoViewActivity extends AppCompatActivity implements View.OnClickListener {

    private SimpleDraweeView iv_show_fresco;

    private Button btn_jpg;
    private Button btn_gif;
    private Button btn_fresco;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fresco.initialize(getApplicationContext());
        setContentView(R.layout.activity_fresco);
        iv_show_fresco = (SimpleDraweeView) findViewById(R.id.iv_show_fresco);
        // 进度条
        GenericDraweeHierarchyBuilder builder =
                new GenericDraweeHierarchyBuilder(getResources());
        GenericDraweeHierarchy hierarchy = builder
                .setProgressBarImage(new ProgressBarDrawable())
                .build();
        iv_show_fresco.setHierarchy(hierarchy);

        btn_jpg = (Button) findViewById(R.id.btn_jpg);
        btn_jpg.setOnClickListener(this);

        btn_gif = (Button) findViewById(R.id.btn_gif);
        btn_gif.setOnClickListener(this);

        btn_fresco = (Button) findViewById(R.id.btn_fresco);
        btn_fresco.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_jpg:
                Uri uri = Uri.parse("http://c.hiphotos.baidu.com/image/h%3D300/sign=c8fcb684d0ca7bcb627bc12f8e086b3f/a2cc7cd98d1001e983f39b48bf0e7bec55e797ae.jpg");
                iv_show_fresco.setImageURI(uri);
                break;
            case R.id.btn_gif:
                // 图片
                Uri gif_uri = Uri.parse("http://i.imgur.com/IHiXGND.gif");
                DraweeController controller = Fresco.newDraweeControllerBuilder()
                        .setUri(gif_uri)
                        .setAutoPlayAnimations(true)
                        .setControllerListener(controllerListener)
                        .build();
                iv_show_fresco.setController(controller);
                break;
            case R.id.btn_fresco:
                Uri web_uri = Uri.parse("http://fresco-cn.org/");
                Intent intent = new Intent(Intent.ACTION_VIEW, web_uri);
                startActivity(intent);
                break;
        }
    }

    ControllerListener controllerListener = new BaseControllerListener<ImageInfo>() {
        @Override
        public void onFinalImageSet(
                String id,
                @Nullable ImageInfo imageInfo,
                @Nullable Animatable anim) {
            if (imageInfo == null) {
                return;
            }
            QualityInfo qualityInfo = imageInfo.getQualityInfo();
            FLog.e("Final image received! " +
                            "Size %d x %d",
                    "Quality level %d, good enough: %s, full quality: %s",
                    imageInfo.getWidth(),
                    imageInfo.getHeight(),
                    qualityInfo.getQuality(),
                    qualityInfo.isOfGoodEnoughQuality(),
                    qualityInfo.isOfFullQuality());
        }

        @Override
        public void onIntermediateImageSet(String id, @Nullable ImageInfo imageInfo) {
            FLog.e("Intermediate image received %s", "");
        }

        @Override
        public void onFailure(String id, Throwable throwable) {
            FLog.e(getClass(), throwable, "Error loading %s", id);
        }
    };

}
