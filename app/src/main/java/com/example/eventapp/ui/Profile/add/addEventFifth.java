package com.example.eventapp.ui.Profile.add;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.example.eventapp.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.viewpagerindicator.CirclePageIndicator;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.UUID;

import static android.app.Activity.RESULT_OK;
import static com.example.eventapp.MainActivity.getDisplay;

public class addEventFifth extends Fragment {

    private Button btnUpload;
    private ImageView imageView1,imageView2,imageView3,fullscreen,delete;
    private Boolean fullscreen_flag=false;



    private Uri[] uploadFilesUri = new Uri[3];
    private boolean upload_flag=false;
    private int[] icoFiles={
            R.drawable.ic_add_circle_black_24dp,R.drawable.ic_add_circle_black_24dp,R.drawable.ic_add_circle_black_24dp

    };


    private static ViewPager mPager;
    private static int currentPage = 0;
    CirclePageIndicator indicator;
    private static int NUM_PAGES = 0;
   // private static final Integer[] IMAGES;
    private ArrayList<Bitmap> ImagesArray = new ArrayList<Bitmap>();
    private final int PICK_IMAGE_REQUEST = 71;

    FirebaseStorage storage;
    StorageReference storageReference;
    public ViewPager mViewPager;
    int current = 0,height_fullscreen;
    Bitmap[] mResources ;
    Bitmap[] emptyResources;
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        addEventFour.setData();
        addEventThird.setData();
        addEventSecond.setData();
        getData();
        View root = inflater.inflate(R.layout.add_event_fifth_step, container, false);
        CustomPagerAdapter mCustomPagerAdapter = new CustomPagerAdapter(root.getContext());
        View pagerView =inflater.inflate(R.layout.pager_item, container, false);

        btnUpload = (Button) root.findViewById(R.id.done);
        imageView1 = (ImageView) root.findViewById(R.id.image1);
        imageView2 = (ImageView) root.findViewById(R.id.image2);
        imageView3 = (ImageView) root.findViewById(R.id.image3);
        if(uploadFilesUri[0]!=null) {
            imageView1.setImageBitmap(mResources[0]);
            imageView2.setVisibility(View.VISIBLE);
            current=1;

        }
        else
            imageView1.setImageResource(R.drawable.ic_add_circle_black_24dp);
        if(uploadFilesUri[1]!=null) {
            imageView2.setImageBitmap(mResources[1]);
            imageView3.setVisibility(View.VISIBLE);
            current=2;
        }
        else
            imageView2.setImageResource(R.drawable.ic_add_circle_black_24dp);

        if(uploadFilesUri[2]!=null) {
            imageView3.setImageBitmap(mResources[2]);
            current=3;
        }
        else
            imageView3.setImageResource(R.drawable.ic_add_circle_black_24dp);


        delete = (ImageView)root.findViewById(R.id.delete);
        fullscreen=(ImageView)root.findViewById(R.id.fullscreen);



        Bitmap empty1 = BitmapFactory.decodeResource(root.getResources(),
                R.drawable.add);
        Bitmap empty2 = BitmapFactory.decodeResource(root.getResources(),
                R.drawable.add2);
        Bitmap empty3 = BitmapFactory.decodeResource(root.getResources(),
                R.drawable.add3);

        emptyResources = new Bitmap[]{
                empty1,empty2,empty3
        };
        if (mResources[0]==null)
        mResources=emptyResources.clone();
        setData();

        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();

        mViewPager = (ViewPager) root.findViewById(R.id.pager);
        mViewPager.setAdapter(mCustomPagerAdapter);

        final View parent= root.findViewById(R.id.fifth_step_parent);
        View.OnClickListener onClickListener1 = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int current_item = mViewPager.getCurrentItem();
                CustomPagerAdapter mCustomPagerAdapter = new CustomPagerAdapter(getContext());
                switch (v.getId()){
                    case R.id.image1 :
                        if(uploadFilesUri[0]==null)
                            chooseImage();
                        mViewPager.setCurrentItem(0);
                        break;
                    case R.id.image2 :
                        if(uploadFilesUri[1]==null)
                            chooseImage();
                        mViewPager.setCurrentItem(1);
                        break;
                    case R.id.image3 :
                        if(uploadFilesUri[2]==null)
                            chooseImage();
                        mViewPager.setCurrentItem(2);
                        break;
                    case R.id.done :
                        if(checkData())
                        {
                            uploadImage();
                        }
                        else
                        {
                            //Toast.makeText(getContext(), "Заполните все обязательные поля", Toast.LENGTH_SHORT).show();
                        }
                        break;
                        //очень большая логика обработки показа изображения
                    case R.id.fullscreen :
                        if(!fullscreen_flag)
                        {

//                            height_fullscreen=par.height;
//                            par.height = height;
//                            mViewPager.setLayoutParams(par);
//                            fullscreen.setImageResource(R.drawable.ic_fullscreen_exit_black_24dp);
                            fullscreen_flag=true;
                            LayoutInflater li = LayoutInflater.from(getContext());
                            final View promptsView = li.inflate(R.layout.dialog_full_screen, null);

                            AlertDialog.Builder mDialogBuilder = new AlertDialog.Builder(getContext(),R.style.dialogStyle);
                            mPager = (ViewPager) promptsView.findViewById(R.id.pager);
                            indicator = (CirclePageIndicator)
                                    promptsView.findViewById(R.id.indicator);
                            //Настраиваем prompt.xml для нашего AlertDialog:
                            mDialogBuilder.setView(promptsView);
                            mDialogBuilder
                                    .setCancelable(true)
                                    .setNegativeButton("Закрыть",
                                            new DialogInterface.OnClickListener() {
                                                public void onClick(DialogInterface dialog,int id) {
                                                    dialog.cancel();
                                                    fullscreen_flag=false;
                                                    fullscreen.setImageResource(R.drawable.ic_fullscreen_black_24dp);
                                                }
                                            });

                            init(promptsView.getContext());
                            //Создаем AlertDialog:
                            AlertDialog alertDialog = mDialogBuilder.create();

                            //и отображаем его:
                            alertDialog.show();
                            Point size = getDisplay();
                            alertDialog.getWindow().setLayout(size.x,size.y);
                            alertDialog.getWindow().setGravity(Gravity.BOTTOM);

                        }
//                        else{
//                            fullscreen.setImageResource(R.drawable.ic_fullscreen_black_24dp);
////                            par.height = height_fullscreen;
////                            mViewPager.setLayoutParams(par);
//                            fullscreen_flag=false;
//                        }
                         mCustomPagerAdapter = new CustomPagerAdapter(getContext());
                        mViewPager.setAdapter(mCustomPagerAdapter);
                        mViewPager.setCurrentItem(current_item);
                        break;

                    case R.id.delete :
                        current--;
                         if(current_item==2) {
                             uploadFilesUri[2] = null;
                             imageView3.setImageResource(R.drawable.ic_add_circle_black_24dp);
                             mResources[current_item]=emptyResources[current_item];

                         }
                         else if(current_item==1){
                             if(uploadFilesUri[2]!=null){
                                 uploadFilesUri[1]=uploadFilesUri[2];
                                 uploadFilesUri[2]=null;
                                 imageView3.setImageResource(R.drawable.ic_add_circle_black_24dp);
                                 mResources[current_item]=mResources[2];
                                 imageView2.setImageBitmap(mResources[2]);
                                 mResources[2]=emptyResources[2];
                             }
                             else{
                                 uploadFilesUri[1]=null;
                                 imageView2.setImageResource(R.drawable.ic_add_circle_black_24dp);
                                 mResources[current_item]=emptyResources[current_item];
                                 imageView3.setVisibility(View.INVISIBLE);
                             }

                         }
                         else{
                             if(uploadFilesUri[2]!=null){
                                 uploadFilesUri[0]=uploadFilesUri[2];
                                 uploadFilesUri[2]=null;
                                 imageView3.setImageResource(R.drawable.ic_add_circle_black_24dp);
                                 mResources[current_item]=mResources[2];
                                 imageView1.setImageBitmap(mResources[2]);
                                 mResources[2]=emptyResources[2];
                             }
                             else if(uploadFilesUri[1]!=null){
                                 uploadFilesUri[0]=uploadFilesUri[1];
                                 uploadFilesUri[1]=null;
                                 imageView3.setVisibility(View.INVISIBLE);
                                 imageView2.setImageResource(R.drawable.ic_add_circle_black_24dp);
                                 mResources[current_item]=mResources[1];
                                 imageView1.setImageBitmap(mResources[1]);
                                 mResources[1]=emptyResources[1];
                             }
                             else{
                                 uploadFilesUri[0]=null;
                                 imageView1.setImageResource(R.drawable.ic_add_circle_black_24dp);
                                 mResources[current_item]=emptyResources[current_item];
                                 imageView2.setVisibility(View.INVISIBLE);
                             }
                         }
                         mCustomPagerAdapter = new CustomPagerAdapter(getContext());
                        mViewPager.setAdapter(mCustomPagerAdapter);
                        mViewPager.setCurrentItem(0);

                         break;


                }

            }

        };
         btnUpload.setOnClickListener(onClickListener1);
         imageView2.setOnClickListener(onClickListener1);
         imageView1.setOnClickListener(onClickListener1);
         imageView3.setOnClickListener(onClickListener1);
         fullscreen.setOnClickListener(onClickListener1);
         delete.setOnClickListener(onClickListener1);


        return root;
    }
    private void setData(){
        AddEventViewModel.setImages(mResources);
        AddEventViewModel.setImages_path(uploadFilesUri);
    }
    private void getData(){
        mResources=AddEventViewModel.getImages();
        uploadFilesUri=AddEventViewModel.getImages_path();
    }
    private void chooseImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK
                && data != null && data.getData() != null )
        {
            uploadFilesUri[current] = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContext().getContentResolver(), uploadFilesUri[current]);
                mResources[current]=bitmap;
                switch (current){
                    case 0:
                        imageView1.setImageBitmap(bitmap);
                        imageView2.setVisibility(View.VISIBLE);
                        current++;
                        break;
                    case 1:
                        imageView2.setImageBitmap(bitmap);
                        imageView3.setVisibility(View.VISIBLE);
                        current++;
                        break;
                    case 2:
                        imageView3.setImageBitmap(bitmap);
                        current++;
                        break;
                }
                CustomPagerAdapter mCustomPagerAdapter = new CustomPagerAdapter(getContext());
                mViewPager.setAdapter(mCustomPagerAdapter);
                setData();

            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }
    }
    int key=0;
    private void uploadImage() {

        for(int i = 0;i<uploadFilesUri.length;i++) {

            if(uploadFilesUri[i] != null)
        {
            final ProgressDialog progressDialog = new ProgressDialog(getContext());
            progressDialog.setTitle("Секундочку...");
            progressDialog.show();

               final StorageReference ref1 = storageReference.child("images/"+ UUID.randomUUID().toString());
                ref1.putFile(uploadFilesUri[i])
                        .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                progressDialog.dismiss();

                                ref1.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                    @Override
                                    public void onSuccess(Uri uri) {
                                        uploadFilesUri[0]=uri;
                                        key++;
                                        setData();
                                        if(key==1&&uploadFilesUri[1]==null) {
                                            send();
                                            Toast.makeText(getContext(), uri.toString(), Toast.LENGTH_SHORT).show();
                                        }
                                        else if(key==2&&uploadFilesUri[2]==null)
                                            send();
                                        else if(key==3)
                                            send();
                                    }
                                });

                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                progressDialog.dismiss();
                               // Toast.makeText(getContext(), "Failed " + e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        })
                        .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                                double progress = (100.0 * taskSnapshot.getBytesTransferred() / taskSnapshot
                                        .getTotalByteCount());
                                progressDialog.setMessage("Загрузка " + (int) progress + "%");
                            }
                        });
            }
        }

    }
public void send(){
    setData();
    try {
        AddEventViewModel vm=new AddEventViewModel();
        //Toast.makeText(getContext(), uploadFilesUri[0].toString(), Toast.LENGTH_SHORT).show();
        vm.sendData();
        Navigation.findNavController(getActivity(),R.id.nav_host_fragment).popBackStack();
        Navigation.findNavController(getActivity(),R.id.nav_host_fragment).navigate(R.id.nav_profile);


    } catch (ParseException e) {
        e.printStackTrace();
    }
}
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    getData();
    }



    class CustomPagerAdapter extends PagerAdapter {

        Context mContext;
        LayoutInflater mLayoutInflater;

        public CustomPagerAdapter(Context context) {
            mContext = context;
            mLayoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }


        @Override
        public int getCount() {
            return mResources.length;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == ((LinearLayout) object);
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            View itemView = mLayoutInflater.inflate(R.layout.pager_item, container, false);

            ImageView imageView = (ImageView) itemView.findViewById(R.id.imageView);

                imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);

            imageView.setImageBitmap(mResources[position]);

            container.addView(itemView);

            return itemView;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((LinearLayout) object);
        }
    }
    private boolean checkData(){
        addEventFour.setData();
        addEventThird.setData();
        addEventSecond.setData();
        if(AddEventViewModel.getDate()==null||AddEventViewModel.getTime()==null||AddEventViewModel.getPrice()==-1||AddEventViewModel.getAddress()==null){
            addEvent.next(2);
            return false;
        }
        else if(AddEventViewModel.getName()==null||AddEventViewModel.getAbout()==null){
            addEvent.next(1);
            return false;
        }
        else if(AddEventViewModel.getType()==null){
            addEvent.next(0);
            return false;
        }
        else if(AddEventViewModel.getImages()[0]==null||AddEventViewModel.getImages_path()[0]==null){
            return false;
        }
        else
        {
            return true;
        }
    }
    private void init(Context c) {
        ImagesArray.clear();
        for(int i=0;i<mResources.length;i++)
            ImagesArray.add(mResources[i]);

        mPager.setAdapter(new SlidingImage_Adapter(c,ImagesArray));



        indicator.setViewPager(mPager);

        final float density = getResources().getDisplayMetrics().density;

//Set circle indicator radius
        indicator.setRadius(5 * density);

        NUM_PAGES =mResources.length;

        // Auto start of viewpager
        final Handler handler = new Handler();
        final Runnable Update = new Runnable() {
            public void run() {
                if (currentPage == NUM_PAGES) {
                    currentPage = 0;
                }
                mPager.setCurrentItem(currentPage++, true);
            }
        };
//        Timer swipeTimer = new Timer();
//        swipeTimer.schedule(new TimerTask() {
//            @Override
//            public void run() {
//                handler.post(Update);
//            }
//        }, 3000, 3000);

        // Pager listener over indicator
        indicator.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageSelected(int position) {
                currentPage = position;

            }

            @Override
            public void onPageScrolled(int pos, float arg1, int arg2) {

            }

            @Override
            public void onPageScrollStateChanged(int pos) {

            }
        });

    }



}
