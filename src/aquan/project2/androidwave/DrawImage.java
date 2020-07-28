package aquan.project2.androidwave;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Environment;
import android.util.Log;

public class DrawImage {
	
	private static final String TAG = null;
	//private static final String TAG = null;
	int count = 0;
	public Bitmap bmpBase = null;
//	private Context context;
	
	public DrawImage() throws IOException {
		FileOutputStream fos = null;
//		context = null;
//		WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
//	    Display display = wm.getDefaultDisplay();
//	    DisplayMetrics metrics = new DisplayMetrics();
//	    display.getMetrics(metrics);
//	    int width = metrics.widthPixels;
//	    int height = metrics.heightPixels;

		bmpBase = Bitmap.createBitmap(720, 1280, Bitmap.Config.ARGB_8888);
		
		Canvas canvas = new Canvas(bmpBase);
		// draw what ever you want canvas.draw...
		canvas.drawColor(Color.WHITE);
		
       	float x1, x2;
       	float y = 0;
       	Paint paintdB = new Paint();
       	paintdB.setColor(Color.BLUE);
       	paintdB.setStyle(Paint.Style.FILL);
       	//canvas.drawLine(360, 0, 360, canvas.getHeight(), paintdB);
       	
       	try {
       		String filePath = Environment.getExternalStorageDirectory() + "/Sound Recordings";
            //RandomAccessFile file = new RandomAccessFile(filePath,"r");
            
            File fl = new File(filePath);
    	    File[] files = fl.listFiles(new FileFilter() {          
    	        public boolean accept(File file) {
    	            return file.isFile();
    	        }
    	    });
    	    long lastMod = Long.MIN_VALUE;
    	    File choice = null;
    	    for (File file0 : files) {
    	        if (file0.lastModified() > lastMod) {
    	            choice = file0;
    	            lastMod = file0.lastModified();
    	        }
    	    }
    	    //String imgPath = "/mnt/sdcard/Waveform/" + choice.getName() + ".jpg";
            //Log.d(TAG,"Image path: " + imgPath);
    	    ByteArrayOutputStream out = new ByteArrayOutputStream();
           	BufferedInputStream in = new BufferedInputStream(new FileInputStream(choice.getAbsolutePath()));

           	int read;
           	byte[] buff = new byte[1024];
           	while ((read = in.read(buff)) > 0)
           	{
           	    out.write(buff, 0, read);
           	}
           	out.flush();
           	byte[] audioBytes = out.toByteArray();
           	
           	int i;
           	int len1 = audioBytes.length;
           	short[] samp = new short[len1/2];
           	for(i = 0 ; i < (len1-1)/2 ; i++)
           	{
           		samp[i] = (short) ((audioBytes[i*2] & 0xFF) | (audioBytes[i*2+1] << 8));
           		//if (samp[i]>50 || samp[i]<-50) 
           			samp[i] /= 91;
           	}
           	
           	int len2 = samp.length;
           	float d = (float) ((canvas.getHeight())*(1.0/len2));
                     	
           	for (i = 0 ; i<len2-2 ; i++)
           	{
           		//Log.d(TAG, "Data = " + audioBytes[i] + "  " + i);
           		x1 = (float) (samp[i]);
           		x2 = (float) (samp[i+1]);
           		canvas.drawLine(360 + x1, y, 360 + x2, y+d, paintdB);
           		y += d;
           	}
        } catch (FileNotFoundException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }

		// Save Bitmap to File
       	try
		{
       		String filePath1 = Environment.getExternalStorageDirectory() + "/Sound Recordings";
            //RandomAccessFile file = new RandomAccessFile(filePath,"r");
            
            File fl1 = new File(filePath1);
    	    File[] files1 = fl1.listFiles(new FileFilter() {          
    	        public boolean accept(File file1) {
    	            return file1.isFile();
    	        }
    	    });
    	    long lastMod1 = Long.MIN_VALUE;
    	    File choice1 = null;
    	    for (File file1 : files1) {
    	        if (file1.lastModified() > lastMod1) {
    	            choice1 = file1;
    	            lastMod1 = file1.lastModified();
    	        }
    	    }
    	    String imgPath = "/mnt/sdcard/Waveform/" + choice1.getName() + ".jpg";
            //Log.d(TAG,"Image path: " + imgPath);
            
		    fos = new FileOutputStream(imgPath);
		    bmpBase.compress(Bitmap.CompressFormat.PNG, 100, fos);

		    fos.flush();
		    fos.close();
		    fos = null;
		}
		catch (IOException e)
		{
		    e.printStackTrace();
		}
		finally
		{
		    if (fos != null)
		    {
		        try
		        {
		            fos.close();
		            fos = null;
		        }
		        catch (IOException e)
		        {
		            e.printStackTrace();
		        }
		    }
		}	
    }
}
