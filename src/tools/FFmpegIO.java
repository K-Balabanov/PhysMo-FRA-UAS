/*
 * Tool to convert videos to a series of images in a specified project directory or a series of images to a video
 * also require this to convert between video formats so that they are editable in other programs
 */
package tools;

import main.*;

import java.io.*;
import javax.swing.JProgressBar;

/**
 *
 * @author jasonkb
 */
public class FFmpegIO 
{
    JProgressBar prog;
    public FFmpegIO(JProgressBar pb)
    {
        this.prog = pb;
        
    }
    
    /**
     * 
     * @param videoPath
     * @return Vector with position 0 the time in seconds and position 1 the FPS
     */
    public double[] getProperties(String videoPath)
    {
        double[] properties = new double[2];
        double seconds = 0.0;
        double fps = 0.0;
        String s = "";
        String information = "";
             
        
        
        
        
        
        if(PhysMo.windows){
        try
        {
            File dir1 = new File (".");
            System.out.println ("Current dir : " + dir1.getCanonicalPath());
     
            
            ProcessBuilder pb = new ProcessBuilder(new String[]{"ffmpeg.exe","-i","\""+videoPath+"\""});
            pb.redirectErrorStream(true);
            Process p = null;
            
            p= pb.start();
            BufferedReader processOutputReader = null; 
            BufferedReader processErrorReader = null; 
            int character; 
            StringBuilder processLog = new StringBuilder(); 

            processOutputReader = new BufferedReader(new InputStreamReader(p.getInputStream())); 
                        while ((character = processOutputReader.read()) != -1) 
                        { 
                                processLog.append((char) character); 
                        } 
                        processLog.append('\n'); 

                        processErrorReader = new BufferedReader(new InputStreamReader(p.getErrorStream())); 
                        while ((character = processErrorReader.read()) != -1) 
                        { 
                                processLog.append((char) character); 
                        }
                        
                        System.out.println(processLog);
                        information = processLog.toString();
                        
             if(processOutputReader != null) 
             { 
                 processOutputReader.close(); 
             }
             if(processErrorReader != null) 
             { 
                 processErrorReader.close(); 
             }
             try
             {
                 p.waitFor();
                 p.destroy();
             }
             catch(InterruptedException e)
             {
                 
             }
            
            
            
            PhysMo.videoInformation = information;
            System.out.println(information);
                    
            String durationString = "";
                    
            String[] items = new String[2];
            items = (information.split("Duration: "));
            for (int i = 0; i < items.length; i++) 
            {
                 System.out.println("Splitting: ");
                 System.out.println(items[i]+"\n");
                 if(i == items.length-1)
                 {
                      durationString = items[i];
                 }
            }
            System.out.println("Items length: "+items.length +" "+items[0]);
            durationString = items[1];
            
            //we now have a string with the duration at the front.  Split the string with _fps so that we have FPS at the end of the duration string
            if(durationString.contains(" fps"))
            {
                items = (durationString.split(" fps"));
            }
            if(durationString.contains(" tbr"))
            {
                items = (durationString.split(" tbr"));
            }
            
            
            System.out.println("The duration string is as follows: "+items[0]);
            durationString = items[0];
            //now split based on ,_ so that the duration is the first term and the fps is the last term in items
            items = (durationString.split(", "));
            durationString = items[0];
            int itemsLength = items.length;
            
            String fpsString = items[itemsLength-1];
            
            System.out.println("Duration of video: "+ durationString+" Frame rate: "+fpsString+"fps");
            
            //now to convert the duration to a double
            items = durationString.split(":");
            System.out.println("Should have 3 in items: "+items.length+ " and the contents are hours min sec " +items);
            double hours = Double.parseDouble(items[0]);
            double minutes = Double.parseDouble(items[1]);
            double sec = Double.parseDouble(items[2]);
            
            seconds = (60*60*hours)+(60*minutes)+sec;
            fps = Double.parseDouble(fpsString);
            
            System.out.println("total sec: "+seconds+" fps: "+fps);
            
            
            
        }
        catch(IOException e)
        {
            System.out.println(e);
        }
        
        
        properties[0] = seconds;
        properties[1] = fps;
        
        return properties;
        
        
        
        
        
        
        }
        else
        {
        
        
        
        
        
        
        
        
        
        
        
        
        
        
        
        
        
        
        
        
        
        
        
        
        
        
        
        
        try
        {
            File dir1 = new File (".");
            System.out.println ("Current dir : " + dir1.getCanonicalPath());
     
            
            System.out.println("will call ffmpeg with the following:");
            //videoPath = "\'"+videoPath+"\'";
            //System.out.println(".\\ffmpeg -i "+videoPath);
            //String runCmd = "ffmpeg -i "+videoPath;
            
            String[] commands = new String[]{"ffmpeg","-i", videoPath};
            
            
            Process videoInfo = Runtime.getRuntime().exec(commands);            
            
            InputStreamReader inputStream = new InputStreamReader(videoInfo.getInputStream());
            BufferedReader stdInput = new BufferedReader(inputStream);
            BufferedReader stdError = new BufferedReader(new InputStreamReader(videoInfo.getErrorStream()));

            while ((s = stdInput.readLine()) != null) 
            {
                 information = information + s;
            }
                    
            while ((s = stdError.readLine()) != null) 
            {
                 information = information + s;
            }
            
            PhysMo.videoInformation = information;
            System.out.println(information);
                    
            String durationString = "";
                    
            String[] items = new String[2];
            items = (information.split("Duration: "));
            for (int i = 0; i < items.length; i++) 
            {
                 System.out.println("Splitting: ");
                 System.out.println(items[i]+"\n");
                 if(i == items.length-1)
                 {
                      durationString = items[i];
                 }
            }
            System.out.println("Items length: "+items.length +" "+items[0]);
            durationString = items[1];
            
            //we now have a string with the duration at the front.  Split the string with _fps so that we have FPS at the end of the duration string
            
            if(durationString.contains(" fps") && durationString.contains(" tbr"))
            {
                items = (durationString.split(" fps"));
            }
            else if(durationString.contains(" fps"))
            {
                items = (durationString.split(" fps"));
            }
            else if(durationString.contains(" tbr"))
            {
                items = (durationString.split(" tbr"));
            }
            
            
            System.out.println("The duration string is as follows: "+items[0]);
            durationString = items[0];
            //now split based on ,_ so that the duration is the first term and the fps is the last term in items
            items = (durationString.split(", "));
            durationString = items[0];
            int itemsLength = items.length;
            
            String fpsString = items[itemsLength-1];
            
            System.out.println("Duration of video: "+ durationString+" Frame rate: "+fpsString+"fps");
            
            //now to convert the duration to a double
            items = durationString.split(":");
            System.out.println("Should have 3 in items: "+items.length+ " and the contents are hours min sec " +items);
            double hours = Double.parseDouble(items[0]);
            double minutes = Double.parseDouble(items[1]);
            double sec = Double.parseDouble(items[2]);
            
            seconds = (60*60*hours)+(60*minutes)+sec;
            fps = Double.parseDouble(fpsString);
            
            System.out.println("total sec: "+seconds+" fps: "+fps);
            
            
            
        }
        catch(IOException e)
        {
            System.out.println(e);
        }
        
        
        properties[0] = seconds;
        properties[1] = fps;
        
        return properties;}
    }
    
    /**
     * Call this method to break a video file down into individual frames.
     * 
     * @param videoPath Absolute path to video that will be decomposed into individual frames
     * @param workingDirectory Directory to dump grabbed frames
     * @param imageType Type of file format that images will be stored as.  PNG default
     */
    public void decomposeVideo(String videoPath, String workingDirectory, String imageType)
    {
        
        
        
        
        
        if(PhysMo.windows)
        {
            try
        {
            File dir1 = new File (".");
             String s = "";
             String information = "";
                   
             if(videoPath.endsWith("3gp"))
             {
                 //perform workaround here converting to avi file format then let rip with decomposition
                 String workingDirectoryCommand = "\""+workingDirectory+"\\workaround.avi"+"\"";
                 ProcessBuilder pb = new ProcessBuilder(new String[]{"ffmpeg.exe","-i","\""+videoPath+"\"",workingDirectoryCommand});
            pb.redirectErrorStream(true);
            Process p = null;
            
            p= pb.start();
            BufferedReader processOutputReader = null; 
            BufferedReader processErrorReader = null; 
            int character; 
            StringBuilder processLog = new StringBuilder(); 

            processOutputReader = new BufferedReader(new InputStreamReader(p.getInputStream())); 
                        while ((character = processOutputReader.read()) != -1) 
                        { 
                                processLog.append((char) character); 
                        } 
                        processLog.append('\n'); 

                        processErrorReader = new BufferedReader(new InputStreamReader(p.getErrorStream())); 
                        while ((character = processErrorReader.read()) != -1) 
                        { 
                                processLog.append((char) character); 
                        }
                        
                        System.out.println(processLog);
                        information = processLog.toString();
                        
             if(processOutputReader != null) 
             { 
                 processOutputReader.close(); 
             }
             if(processErrorReader != null) 
             { 
                 processErrorReader.close(); 
             }
             try
             {
                 p.waitFor();
                 p.destroy();
             }
             catch(InterruptedException e)
             {
                 
             }
             }
             
               String workingDirectoryCommand = "\""+workingDirectory+"\\frame%d."+imageType+"\"";
                System.out.println("decomposing video");
                 
             
             if(videoPath.endsWith("3gp"))
             {
                 videoPath = "\""+workingDirectory+"\\workaround.avi";
             }
             ProcessBuilder pb = new ProcessBuilder(new String[]{"ffmpeg.exe","-i","\""+videoPath+"\"",workingDirectoryCommand});
            pb.redirectErrorStream(true);
            Process p = null;
            
            p= pb.start();
            BufferedReader processOutputReader = null; 
            BufferedReader processErrorReader = null; 
            int character; 
            StringBuilder processLog = new StringBuilder(); 

            processOutputReader = new BufferedReader(new InputStreamReader(p.getInputStream())); 
                        while ((character = processOutputReader.read()) != -1) 
                        { 
                                processLog.append((char) character); 
                        } 
                        processLog.append('\n'); 

                        processErrorReader = new BufferedReader(new InputStreamReader(p.getErrorStream())); 
                        while ((character = processErrorReader.read()) != -1) 
                        { 
                                processLog.append((char) character); 
                        }
                        
                        System.out.println(processLog);
                        information = processLog.toString();
                        
             if(processOutputReader != null) 
             { 
                 processOutputReader.close(); 
             }
             if(processErrorReader != null) 
             { 
                 processErrorReader.close(); 
             }
             try
             {
                 p.waitFor();
                 p.destroy();
             }
             catch(InterruptedException e)
             {
                 
             }
             
                
             
             workingDirectoryCommand = "\""+workingDirectory+"\\preview%d."+imageType+"\"";
             
             
             ProcessBuilder pb2 = new ProcessBuilder(new String[]{"ffmpeg.exe","-i","\""+videoPath+"\"","-s","200x200",workingDirectoryCommand});
            pb2.redirectErrorStream(true);
            Process p2 = null;
            
            p2= pb2.start();
            BufferedReader processOutputReader2 = null; 
            BufferedReader processErrorReader2 = null; 
            int character2; 
            StringBuilder processLog2 = new StringBuilder(); 

            processOutputReader2 = new BufferedReader(new InputStreamReader(p2.getInputStream())); 
                        while ((character2 = processOutputReader2.read()) != -1) 
                        { 
                                processLog2.append((char) character2); 
                        } 
                        processLog2.append('\n'); 

                        processErrorReader2 = new BufferedReader(new InputStreamReader(p2.getErrorStream())); 
                        while ((character2 = processErrorReader2.read()) != -1) 
                        { 
                                processLog2.append((char) character2); 
                        }
                        
                        System.out.println(processLog2);
                        information = processLog2.toString();
                        
             if(processOutputReader2 != null) 
             { 
                 processOutputReader2.close(); 
             }
             if(processErrorReader2 != null) 
             { 
                 processErrorReader2.close(); 
             }
             try
             {
                 p2.waitFor();
                 p2.destroy();
             }
             catch(InterruptedException e)
             {
                 
             }
             
              
                    
        }
        catch(java.io.IOException error)
        {        
            return;
        }
        return;
    
            
            
            
        }
        else{
        
        
        
        
        
        
        
        
        
        
        
        
        
        
        
        
        
        try
        {
            File dir1 = new File (".");
             String s = "";
             String information = "";
              
             if(videoPath.endsWith("3gp"))
             {
                 //perform workaround here
                 String workingDirectoryCommand = workingDirectory+"/workaround.avi";
                 String[] commands = new String[]{"ffmpeg","-i", videoPath, workingDirectoryCommand};
                 Process p = Runtime.getRuntime().exec(commands);
                 try
                 {
                     p.waitFor();
                 }
                 catch(InterruptedException inter)
                 {
            
                 }
                 videoPath = workingDirectory+"/workaround.avi";
             }
             
             String workingDirectoryCommand = workingDirectory+"/frame%d."+imageType;
             
             
             
             String[] commands = new String[]{"ffmpeg","-i", videoPath, workingDirectoryCommand};
             
             
                
             Process p = Runtime.getRuntime().exec(commands);
             
             workingDirectoryCommand = workingDirectory+"/preview%d."+imageType;
             String[] commands2 = new String[]{"ffmpeg","-i", videoPath, "-s","200x200", workingDirectoryCommand};
             
             
                
             Process p2 = Runtime.getRuntime().exec(commands2);
             InputStreamReader inputStream = new InputStreamReader(p2.getInputStream());
             
             
             BufferedReader stdInput = new BufferedReader(inputStream);
             BufferedReader stdError = new BufferedReader(new InputStreamReader(p2.getErrorStream()));

                    // read the output from the command
                    //System.out.println("Here is the standard output of the command:\n");
             while ((s = stdInput.readLine()) != null) 
             {
                        System.out.println(s);
                  information = information + s;
             }
             
             while ((s = stdError.readLine()) != null) 
             {
                 
                  information = information + s;
             }
                    
                    System.out.println("This is the test post preview generation: "+information);
                    
                    
        }
        catch(java.io.IOException error)
        {        
            return;
        }
        
        return;
    }
    
    }
}
