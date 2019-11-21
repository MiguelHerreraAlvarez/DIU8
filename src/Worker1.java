import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;
import javax.swing.SwingWorker;

class Worker1 extends SwingWorker<Double, Integer> {
    private static final int BUFFER_SIZE = 4096;
    private List<String> nextFiles = null;    
    private String destino = null;
    private BufferedInputStream origin = null;
    private int orig;
   
   @Override
    protected Double doInBackground() throws Exception {   
        try{
            List<String> files = nextFiles;             
            FileOutputStream dest = new FileOutputStream(destino);

            ZipOutputStream out = new ZipOutputStream(new BufferedOutputStream(dest));
            byte[] data = new byte[BUFFER_SIZE];
            
            Iterator i = files.iterator();
            int counter = 0;
            while(i.hasNext())
            {
                counter++;
                setProgress((counter*100/files.size()));
                String filename = (String)i.next();
                FileInputStream fi = new FileInputStream(filename);
                origin = new BufferedInputStream(fi, BUFFER_SIZE);
                ZipEntry entry = new ZipEntry(filename.substring(orig));
                out.putNextEntry( entry );
                int count;
                while( !isCancelled() && (count = origin.read(data, 0, BUFFER_SIZE)) != -1){
                    out.write(data, 0, count);
                }
                origin.close();
                if(isCancelled()) break;
                
            }
            out.close();
        }
        catch( IOException e ){
            e.printStackTrace();
        }
        return 100.0;
    }
    
    public void setFiles(List<String> f, int orig){
        nextFiles = f;
        this.orig = orig;
    }
    public void setDestination(String s){
        destino = s;
    } 

    public void cancel() throws IOException{
        cancel(true);
    }
}
