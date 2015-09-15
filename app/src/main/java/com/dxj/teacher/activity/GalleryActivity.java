package com.dxj.teacher.activity;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.GlideDrawableImageViewTarget;
import com.dxj.teacher.R;
import com.dxj.teacher.base.BaseActivity;
import com.dxj.teacher.utils.MyUtils;
import com.dxj.teacher.utils.ZoomOutPageTransformer;
import com.dxj.teacher.widget.TitleNavBar;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;

import uk.co.senab.photoview.PhotoView;
import uk.co.senab.photoview.PhotoViewAttacher;

/**
 * 图片浏览器
 */
public class GalleryActivity extends BaseActivity {
    private static final String EXTERNAL_PATH = Environment.getExternalStorageDirectory().getAbsolutePath();
    private static final String SAVE_PIC_PATH = EXTERNAL_PATH + "/ihome/image/";
    private static final String CURRENT_VISIBLE_PAGE = "currentPage";
    private HashSet<ViewGroup> unRecycledViews = new HashSet<ViewGroup>();
    private List<String> urls;
    private ViewPager pager;


    private int index;
    //    private SalesmanBean salesmanBean;


    // private String imgUrlPath;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.galleryactivity_layout);
        initTitle();
        initData();
        initView();

    }

    @Override
    public void initTitle() {
        TitleNavBar title = (TitleNavBar) findViewById(R.id.title);
        title.setTitle("相册");
        title.setOnTitleNavClickListener(new TitleNavBar.OnTitleNavClickListener() {
            @Override
            public void onNavOneClick() {

            }

            @Override
            public void onNavTwoClick() {

            }

            @Override
            public void onNavThreeClick() {

            }

            @Override
            public void onActionClick() {

            }

            @Override
            public void onBackClick() {

            }
        });
    }

    @Override
    public void initView() {
        pager = (ViewPager) findViewById(R.id.pager);
        pager.setAdapter(new ImagePagerAdapter());
        pager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                // TODO Auto-generated method stub
                super.onPageSelected(position);
            }
        });
        pager.setPageTransformer(true, new ZoomOutPageTransformer());
        pager.setCurrentItem(index);
    }

    @Override
    public void initData() {
        urls = getIntent().getStringArrayListExtra("imgPath");
        index = getIntent().getIntExtra("index", 0);

    }

//    }

    @Override
    protected void onStart() {
        // TODO Auto-generated method stub
        super.onStart();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // TODO Auto-generated method stub
        if (item.getItemId() == android.R.id.home) {
            GalleryActivity.this.finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {
        // TODO Auto-generated method stub
        super.onCreateContextMenu(menu, v, menuInfo);
    }


    @Override
    public void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
        MyUtils.recycleViewGroupAndChildViews(pager, true);
        for (ViewGroup viewGroup : unRecycledViews) {
            MyUtils.recycleViewGroupAndChildViews(viewGroup, true);
        }
        System.gc();
    }


    private class ImagePagerAdapter extends PagerAdapter {
        private LayoutInflater inflater;

        public ImagePagerAdapter() {
            inflater = getLayoutInflater();
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            // TODO Auto-generated method stub
            if (object instanceof ViewGroup) {
                ((ViewPager) container).removeView((View) object);
                unRecycledViews.remove(object);
                ViewGroup viewGroup = (ViewGroup) object;
                MyUtils.recycleViewGroupAndChildViews(viewGroup, true);
            }
        }

        @Override
        public int getCount() {
            // TODO Auto-generated method stub
            return urls.size();
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            // TODO Auto-generated method stub
            View contentView = inflater.inflate(R.layout.galleryactivity_item, container, false);
            handlePage(position, contentView, true);
            ((ViewPager) container).addView(contentView, 0);
            unRecycledViews.add((ViewGroup) contentView);
            return contentView;
        }

        @Override
        public boolean isViewFromObject(View arg0, Object arg1) {
            // TODO Auto-generated method stub
            return arg0.equals(arg1);
        }

        @Override
        public void setPrimaryItem(View container, int position, Object object) {
            // TODO Auto-generated method stub
            super.setPrimaryItem(container, position, object);
            View contentView = (View) object;
            if (contentView == null) {
                return;
            }
            container.setTag(CURRENT_VISIBLE_PAGE);
            ImageView imageView = (ImageView) contentView.findViewById(R.id.image);

            if (imageView.getDrawable() != null) {
                return;
            }
        }
    }

    private void handlePage(int position, View contentView, boolean fromInstantiateItem) {
        final PhotoView imageView = (PhotoView) contentView.findViewById(R.id.image);
        final ProgressBar mProgress = (ProgressBar) contentView.findViewById(R.id.anim_progress);
        imageView.setVisibility(View.VISIBLE);
        imageView.setDrawingCacheEnabled(true);
        final String path = urls.get(position);
        Log.i("TAG", "path=" + path);
        Glide.with(GalleryActivity.this).load(path).into(new GlideDrawableImageViewTarget(imageView) {
            @Override
            public void onStart() {
                // TODO Auto-generated method stub
                super.onStart();
            }

            @Override
            public void onResourceReady(GlideDrawable arg0, GlideAnimation<? super GlideDrawable> arg1) {
                // TODO Auto-generated method stub
                super.onResourceReady(arg0, arg1);
                mProgress.setVisibility(View.GONE);
            }
        });
        imageView.setOnPhotoTapListener(new PhotoViewAttacher.OnPhotoTapListener() {

            @Override
            public void onPhotoTap(View view, float x, float y) {
                // TODO Auto-generated method stub
//		if (salesmanBean == null) {
//		    GalleryActivity.this.finish();
//		} else {
//		    if (isShow) {
//			isShow = false;
//			animateHide();
//		    } else {
//			isShow = true;
//			animateBack();
//		    }
//		}
            }
        });
//        imageView.setOnLongClickListener(new View.OnLongClickListener() {
//
//            @Override
//            public boolean onLongClick(View v) {
//                // TODO Auto-generated method stub
//                DialogFragment dialog = new FireMissilesDialogFragment(path, imageView);
//                dialog.show(getSupportFragmentManager(), "NoticeDialogFragment");
//                // save(path, imageView);
//                return false;
//            }
//        });
    }

//    public void save(String qrcode, PhotoView imageView) { // 保存图片到相册
//        // AlertDialog.
//        File folder = new File(SAVE_PIC_PATH);
//        if (!folder.exists()) {
//            folder.mkdirs();
//        }
//        int length = qrcode.split("/").length;
//        String fileName = qrcode.split("/")[length - 1];
//        File file = new File(folder, fileName);
//        Log.i("TAG", file.getAbsolutePath());
//        BufferedOutputStream bos = null;
//        try {
//            if (!file.exists()) {
//                file.createNewFile();
//            } else {
//                file.delete();
//            }
//            bos = new BufferedOutputStream(new FileOutputStream(file));
//            Log.i("TAG", "imageViews=" + imageView.getDrawingCache());
//            Bitmap bitmap = imageView.getDrawingCache().copy(Config.RGB_565, false); // getDrawingCache拿到的bitmap要copy一下，直接用会报java.lang.IllegalStateException: Can't compress a recycled bitmap
//
//            Log.i("TAG", bitmap + "");
//            // iv_qrcode.setDrawingCacheEnabled(false);
//            if (bitmap != null) {
//                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bos);
//            }
//        } catch (IOException e) {
//            e.printStackTrace();
//        } finally {
//            try {
//                bos.flush();
//                bos.close();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }
//        // 通知系统更新相册
//        Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
//        Uri uri = Uri.fromFile(file);
//        intent.setData(uri);
//        sendBroadcast(intent);
//    }

//    public class FireMissilesDialogFragment extends DialogFragment {
//        private String qrcode;
//        private PhotoView imageView;
//
//        public FireMissilesDialogFragment(String qrcode, PhotoView imageView) {
//            // TODO Auto-generated constructor stub
//            this.qrcode = qrcode;
//            this.imageView = imageView;
//        }
//
//        @Override
//        public Dialog onCreateDialog(Bundle savedInstanceState) {
//            // Use the Builder class for convenient dialog construction
//            android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(getActivity());
//            builder.setMessage("保存图片到相册吗？").setPositiveButton("确定", new DialogInterface.OnClickListener() {
//                public void onClick(DialogInterface dialog, int id) {
//                    // FIRE ZE MISSILES!
//                    save(qrcode, imageView);
//                }
//            }).setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
//                public void onClick(DialogInterface dialog, int id) {
//                    // User cancelled the dialog
//                }
//            });
//            // Create the AlertDialog object and return it
//            return builder.create();
//        }
//    }


}
