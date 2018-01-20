/*
returnModel - computer model of return in financial markets based on
V. Gontis, J. Ruseckas, A. Kononovicius "Long-range memory stochastic
model of the return in financial markets" (arXiv:0901.0903).
Software copyright (C) 2009 Aleksejus Kononovicius

This program is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with this program. If not, see <http://www.gnu.org/licenses/>.
*/

import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import javax.swing.*;
import java.util.Random;
import java.math.*;
import java.math.BigDecimal;
import java.io.*;
import javax.imageio.ImageIO;
import java.net.URL;

/*main class*/
public class returnModel {
	//program parameters
	public static boolean performCalculations=true;
	public static boolean useGraphicalInterface=true;
	public static boolean commandLine=false;
	public static boolean noOut=false;
	public static int graphicslFileOut=-1;// -1 - none, 0 - png, 1 - svg
	public static boolean approxDrop=false;
	public static boolean outTxt=false;
	public static boolean approx=true;
	//changes program settings based on command line input
	public static void getCommandLine(String[] str) {
		int i=0;
		try {
			for(i=0;i<str.length;i++) {
				if(str[i].equalsIgnoreCase("--xmax") || str[i].equalsIgnoreCase("-x")) {
					i++;
					commonVariables.xmax=Double.parseDouble(str[i]);
				} else if(str[i].equalsIgnoreCase("--approxDrop") || str[i].equalsIgnoreCase("-ad")) {
					approxDrop=true;
				} else if(str[i].equalsIgnoreCase("--approx") || str[i].equalsIgnoreCase("-a")) {
					i++;
					if(str[i].equalsIgnoreCase("no")) {
						approx=false;
					} else {
						approx=true;
						commonVariables.approx[0]=Double.parseDouble(str[i]);
						i++;
						commonVariables.approx[1]=Double.parseDouble(str[i]);
						i++;
						commonVariables.approx[2]=Double.parseDouble(str[i]);
						i++;
						commonVariables.approx[3]=Double.parseDouble(str[i]);
						i++;
						commonVariables.approx[4]=Double.parseDouble(str[i]);
						i++;
						commonVariables.approx[5]=Double.parseDouble(str[i]);
						i++;
						commonVariables.approx[6]=Double.parseDouble(str[i]);
						i++;
						commonVariables.approx[7]=Double.parseDouble(str[i]);
					}
				} else if(str[i].equalsIgnoreCase("--image") || str[i].equalsIgnoreCase("-i")) {
					useGraphicalInterface=false;
					i++;
					commonVariables.graphWidth=Integer.parseInt(str[i]);
					i++;
					commonVariables.graphHeight=Integer.parseInt(str[i]);
					i++;
					if(str[i].equalsIgnoreCase("png")) {
						graphicslFileOut=0;
					} else if(str[i].equalsIgnoreCase("svg")) {
						graphicslFileOut=1;
					} else {
						System.out.println("Illegal value supplied with --image option. It shouldn't be \""+str[i]+"\", but should be \"png\" or \"svg\".");
						System.out.println("Assuming default value \"png\" and continuing.");
						graphicslFileOut=0;
					}
				} else if(str[i].equalsIgnoreCase("--window") || str[i].equalsIgnoreCase("-w")) {
					i++;
					commonVariables.graphWidth=Integer.parseInt(str[i]);
					i++;
					commonVariables.graphHeight=Integer.parseInt(str[i]);
					i++;
					if(str[i].equalsIgnoreCase("centered") || str[i].equalsIgnoreCase("c")) {
						commonVariables.windowCentered=true;
					} else if(str[i].equalsIgnoreCase("notCentered") || str[i].equalsIgnoreCase("nC")) {
						commonVariables.windowCentered=false;
					} else {
						System.out.println("Illegal value supplied with --window option. It shouldn't be \""+str[i]+"\", but should be \"centered\", \"c\", \"notCentered\" or \"nC\".");
						System.out.println("Assuming default value \"centered\" and continuing.");
						commonVariables.windowCentered=true;
					}
				} else if(str[i].equalsIgnoreCase("--outPoints") || str[i].equalsIgnoreCase("-op")) {
					i++;
					commonVariables.outPoints=Integer.parseInt(str[i]);
				} else if(str[i].equalsIgnoreCase("--pdfMax") || str[i].equalsIgnoreCase("-pmx")) {
					i++;
					commonVariables.pdfMax=Double.parseDouble(str[i]);
				} else if(str[i].equalsIgnoreCase("--pdfMin") || str[i].equalsIgnoreCase("-pmn")) {
					i++;
					commonVariables.pdfScale=Double.parseDouble(str[i]);
				} else if(str[i].equalsIgnoreCase("--points") || str[i].equalsIgnoreCase("-p")) {
					i++;
					commonVariables.realPoints=Integer.parseInt(str[i]);
				} else if(str[i].equalsIgnoreCase("--realizations") || str[i].equalsIgnoreCase("-r")) {
					i++;
					commonVariables.realizations=Integer.parseInt(str[i]);
				} else if(str[i].equalsIgnoreCase("--lambda") || str[i].equalsIgnoreCase("-l")) {
					i++;
					commonVariables.lambda=Double.parseDouble(str[i]);
				} else if(str[i].equalsIgnoreCase("--lambda2") || str[i].equalsIgnoreCase("-l2")) {
					i++;
					commonVariables.lambda2=Double.parseDouble(str[i]);
				} else if(str[i].equalsIgnoreCase("--r0b") || str[i].equalsIgnoreCase("-rb")) {
					i++;
					commonVariables.r0b=Double.parseDouble(str[i]);
				} else if(str[i].equalsIgnoreCase("--r0a") || str[i].equalsIgnoreCase("-ra")) {
					i++;
					commonVariables.r0a=Double.parseDouble(str[i]);
				} else if(str[i].equalsIgnoreCase("--oldLimiter") || str[i].equalsIgnoreCase("-ol")) {
					commonVariables.oldDiffusionLimiter=true;
				} else if(str[i].equalsIgnoreCase("--noNoise") || str[i].equalsIgnoreCase("-nN")) {
					commonVariables.noise=false;
				} else if(str[i].equalsIgnoreCase("--epsilon") || str[i].equalsIgnoreCase("-e")) {
					i++;
					commonVariables.epsilon=Double.parseDouble(str[i]);
				} else if(str[i].equalsIgnoreCase("--eta") || str[i].equalsIgnoreCase("-n")) {
					i++;
					commonVariables.eta=Double.parseDouble(str[i]);
				} else if(str[i].equalsIgnoreCase("--kappa") || str[i].equalsIgnoreCase("-k")) {
					i++;
					commonVariables.kappa=Double.parseDouble(str[i]);
				} else if(str[i].equalsIgnoreCase("--superTau") || str[i].equalsIgnoreCase("-st")) {
					i++;
					commonVariables.tau=Double.parseDouble(str[i]);
					commonVariables.tau60=commonVariables.tau;
					commonVariables.deltaT=commonVariables.tau;
					commonVariables.kappa=Math.sqrt(commonVariables.tau/0.02);
					int tmpMult=0;
					double tmpNum=commonVariables.kappa;
					while(tmpNum<1) {
						tmpMult++;
						tmpNum*=10;
					}
					commonVariables.kappa=commonFunctions.round(commonVariables.kappa,4+tmpMult);
				} else if(str[i].equalsIgnoreCase("--tau") || str[i].equalsIgnoreCase("-t")) {
					i++;
					commonVariables.tau=Double.parseDouble(str[i]);
				} else if(str[i].equalsIgnoreCase("--tau60") || str[i].equalsIgnoreCase("-t60")) {
					i++;
					commonVariables.tau60=Double.parseDouble(str[i]);
				} else if(str[i].equalsIgnoreCase("--deltaT") || str[i].equalsIgnoreCase("-dT")) {
					i++;
					commonVariables.deltaT=Double.parseDouble(str[i]);
				} else if(str[i].equalsIgnoreCase("--core") || str[i].equalsIgnoreCase("-c")) {
					i++;
					commonVariables.core=Integer.parseInt(str[i]);
					if(commonVariables.core<1) commonVariables.core=1;
				} else if(str[i].equalsIgnoreCase("--output") || str[i].equalsIgnoreCase("-o")) {
					i++;
					commonVariables.outputStr=str[i];
				} else if(str[i].equalsIgnoreCase("--useLimits") || str[i].equalsIgnoreCase("-uL")) {
					commonVariables.useLimits=true;
				} else if(str[i].equalsIgnoreCase("--simpleSDE") || str[i].equalsIgnoreCase("-sS")) {
					commonVariables.simpleSDE=true;
				} else if(str[i].equalsIgnoreCase("--notMinusMean") || str[i].equalsIgnoreCase("-!mm")) {
					commonVariables.minusMean=false;
				} else if(str[i].equalsIgnoreCase("--version") || str[i].equalsIgnoreCase("-v")) {
					System.out.println("returnModel v101201.");
					performCalculations=false;
				} else if(str[i].equalsIgnoreCase("--license")) {
					System.out.println("returnModel - computer model of return in financial markets based on V. Gontis, J. Ruseckas, A. Kononovicius \"Long-range memory stochastic model of the return in financial markets\" (arXiv:0901.0903)."+commonVariables.lineBreak+"Software copyright (C) 2009 Aleksejus Kononovicius"+commonVariables.lineBreak+""+commonVariables.lineBreak+"This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version."+commonVariables.lineBreak+""+commonVariables.lineBreak+"This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details."+commonVariables.lineBreak+""+commonVariables.lineBreak+"You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.");
					performCalculations=false;
				} else if(str[i].equalsIgnoreCase("--notGui") || str[i].equalsIgnoreCase("-ng") || str[i].equalsIgnoreCase("-!g") || str[i].equalsIgnoreCase("--!gui")) {
					useGraphicalInterface=false;
				} else if(str[i].equalsIgnoreCase("--commandLine") || str[i].equalsIgnoreCase("-cL")) {
					commandLine=true;
					useGraphicalInterface=false;
				} else if(str[i].equalsIgnoreCase("--noOut") || str[i].equalsIgnoreCase("-no")) {
					commandLine=true;
					noOut=true;
					useGraphicalInterface=false;
					System.out.println("--noOut option specified only error reporting output will be shown.");
				} else if(str[i].equalsIgnoreCase("--help") || str[i].equalsIgnoreCase("-h")) {
					System.out.println("This is --help or -h output."+commonVariables.lineBreak+""+commonVariables.lineBreak+commonVariables.lineBreak+"The available options include:"+commonVariables.lineBreak+"  * Direct model parameters:"+commonVariables.lineBreak+"    --lambda or -l  This option sets lambda value used in model equations"+commonVariables.lineBreak+"                    (option followed by 1 double). Might be interpreted as"+commonVariables.lineBreak+"                    power of SDE stationary distribution power law tail."+commonVariables.lineBreak+"    --epsilon or -e This options sets epsilon value used in model equations"+commonVariables.lineBreak+"                    (option followed by 1 double). Might be used to control"+commonVariables.lineBreak+"                    break point in PSD."+commonVariables.lineBreak+"    --eta or -n     This options sets eta value used in model equations"+commonVariables.lineBreak+"                    (option followed by 1 double). Has a meanings of"+commonVariables.lineBreak+"                    stochastic multiplicativity."+commonVariables.lineBreak+"    --kappa or -k   This options sets kappa value used in model equations"+commonVariables.lineBreak+"                    (option followed by 1 double). Model precission parameter."+commonVariables.lineBreak+"    --tau or -t     This options sets tau value used by model (option"+commonVariables.lineBreak+"                    followed by 1 double). Dimensionless time window width"+commonVariables.lineBreak+"                    within which we integrated SDE sollutions."+commonVariables.lineBreak+"    --tau60 or      This options sets tau60 value used by model (option"+commonVariables.lineBreak+"    -t60            followed by 1 double). Dimensionless time corresponding"+commonVariables.lineBreak+"                    to real time 60 seconds."+commonVariables.lineBreak+"    --deltaT or -dT This options sets deltaT value used by model"+commonVariables.lineBreak+"                    (option followed by 1 double). Constant timestep size."+commonVariables.lineBreak+"                    Numerical SDE solutions might become unstable with wrong"+commonVariables.lineBreak+"                    parameter value."+commonVariables.lineBreak+"    --xmax or -x    This option sets differing exponential diffusion limit in"+commonVariables.lineBreak+"                    case of default diffusion limiter or (and) physical limit"+commonVariables.lineBreak+"                    to maximum return value in case if useLimits option"+commonVariables.lineBreak+"                    supplied (option followed by 1 integer)."+commonVariables.lineBreak+"    --oldLimiter    This options tells program to use old diffusion limiter"+commonVariables.lineBreak+"    or -ol          ((x*epsilon^n)^2)."+commonVariables.lineBreak+"    --useLimits     This option tells program to use physical limit while"+commonVariables.lineBreak+"    or -ul          modelling."+commonVariables.lineBreak+"    --simpleSDE     This option tells program to use simple SDE while"+commonVariables.lineBreak+"    or -ss          modelling."+commonVariables.lineBreak+"  * Noise parameters:"+commonVariables.lineBreak+"    --lambda2       This parameter sets lambda value used by noise formula"+commonVariables.lineBreak+"    or -l2          (option followed by 1 double). Basicly power of noise"+commonVariables.lineBreak+"                    distribution power law tail."+commonVariables.lineBreak+"    --r0b or -rb    This parameter sets constant without MA(r) in r0() formula"+commonVariables.lineBreak+"                    near (option followed by 1 double). Constant noise"+commonVariables.lineBreak+"                    \"variance\"."+commonVariables.lineBreak+"    --r0a or -ra    This parameters set constant near MA(r) in r0() formula"+commonVariables.lineBreak+"                    (option followed by 1 double). Variable part of noise"+commonVariables.lineBreak+"                    \"variance\"."+commonVariables.lineBreak+"    --noNoise       This option disables usage of additional noise."+commonVariables.lineBreak+"    or -nn"+commonVariables.lineBreak+"  * Mixed parameters (both model and software related):"+commonVariables.lineBreak+"    --experiment or This option allows to set experiment id (option followed "+commonVariables.lineBreak+"    -ex             by integer). Experiment id can be used in output template."+commonVariables.lineBreak+"                    If not set, experiment id equals Unix time."+commonVariables.lineBreak+"    --core or -c    This options sets number of threads used for concurent"+commonVariables.lineBreak+"                    calculation (option followed by 1 integer). For best"+commonVariables.lineBreak+"                    preformance should equal to number of processor cores"+commonVariables.lineBreak+"                    available on machine."+commonVariables.lineBreak+"    --points or -p  This option sets amount of points in each realization"+commonVariables.lineBreak+"                    (option followed by 1 integer)."+commonVariables.lineBreak+"    --realizations  This option sets amount of realizations calculated"+commonVariables.lineBreak+"    or -r           (option followed by 1 integer)."+commonVariables.lineBreak+"    --outPoints     This option sets amount of values calculated then"+commonVariables.lineBreak+"    or -op          calculating probability density function and spectra"+commonVariables.lineBreak+"                    (option followed by 1 integer)."+commonVariables.lineBreak+"    --pdfMax or     This option sets maximum value outputed then"+commonVariables.lineBreak+"    -pmx            calculating probability density function (option followed"+commonVariables.lineBreak+"                    by 1 integer)."+commonVariables.lineBreak+"    --pdfMin or     This option sets minimum value outputed then"+commonVariables.lineBreak+"    -pmn            calculating probability density function (option followed"+commonVariables.lineBreak+"                    by 1 integer)."+commonVariables.lineBreak+"    --notMinusMean  Options tells software not to subtract mean before"+commonVariables.lineBreak+"    or -!mm         calculating spectral density (might improve overlaping"+commonVariables.lineBreak+"                    with empirical results in term of magnitude)."+commonVariables.lineBreak+"    --approx or -a  This option sets approximation bounds which are used"+commonVariables.lineBreak+"                    then pdf and spectra aproximations are calculated"+commonVariables.lineBreak+"                    (option followed by 8 doubles or 1 string). PDF - first"+commonVariables.lineBreak+"                    two doubles; PSD, not simple SDE - 3rd to 6th double;"+commonVariables.lineBreak+"                    PSD, simple SDE - 7th and 8th double; String - \"no\"."+commonVariables.lineBreak+"    --approxDrop    This option forces output of approximated functions"+commonVariables.lineBreak+"    or -ad          to file. At least seven numbers are outputed to file"+commonVariables.lineBreak+"                    during this operation (pdf a, b coeficients, spectra"+commonVariables.lineBreak+"                    a1, b1, a2, b2 coefiencients and \"break-point\" in"+commonVariables.lineBreak+"                    spectra). Additionaly included numbers are from output"+commonVariables.lineBreak+"                    template. File name is set according to output template."+commonVariables.lineBreak+"  * Directly related to software parameters:"+commonVariables.lineBreak+"    --version or -v This option forces output of version data asociated"+commonVariables.lineBreak+"                    with this program. No modelling is done."+commonVariables.lineBreak+"    --license or -l This option forces output of license data asociated"+commonVariables.lineBreak+"                    with this program. No modelling is done."+commonVariables.lineBreak+"    --window or -w  This option sets window parameters (option followed"+commonVariables.lineBreak+"                    by 2 integers and 1 string). Integers - width and height;"+commonVariables.lineBreak+"                    string can be equal to \"centered\" (or \"c\") or"+commonVariables.lineBreak+"                    \"notCentered\" (or \"nc\"). Doesn't imply graphical mode"+commonVariables.lineBreak+"                    (which is used by default)."+commonVariables.lineBreak+"    --image or -i   This option sets image parameters (option followed"+commonVariables.lineBreak+"                    by 2 integers and 1 string) and forces output to graphical"+commonVariables.lineBreak+"                    file. Integers - width and height; string - \"png\" or"+commonVariables.lineBreak+"                    \"svg\". Implies --notGui option."+commonVariables.lineBreak+"    --notGui or -ng This option disables all windows but calculation progress"+commonVariables.lineBreak+"    or --!gui       window. Calculation results are outputed to text files."+commonVariables.lineBreak+"    or -!g"+commonVariables.lineBreak+"    --commandLine   This option disables all windows. Calculation messages"+commonVariables.lineBreak+"    or -cl          are shown in terminal. Implies --notGui option."+commonVariables.lineBreak+"    --noOut or -no  This option disables all output except for maintance"+commonVariables.lineBreak+"                    output (ex. error messages) and result output to text"+commonVariables.lineBreak+"                    file. Implies --commandLine option."+commonVariables.lineBreak+"    --outTxt or -ot This option tells program to output results as text"+commonVariables.lineBreak+"                    files. Implies --notGui option."+commonVariables.lineBreak+"    --output or -o  This option sets template of output directory and file"+commonVariables.lineBreak+"                    (option followed by 1 string). Some wild cards can be"+commonVariables.lineBreak+"                    used:"+commonVariables.lineBreak+"                      * \"[l]\" is substituted with lambda value,"+commonVariables.lineBreak+"                      * \"[e]\" is substituted with epsilon value,"+commonVariables.lineBreak+"                      * \"[n]\" is substituted with eta value,"+commonVariables.lineBreak+"                      * \"[k]\" is substituted with kappa value,"+commonVariables.lineBreak+"                      * \"[t]\" is substituted with tau value,"+commonVariables.lineBreak+"                      * \"[l2]\" is substituted with lambda2 value,"+commonVariables.lineBreak+"                      * \"[rb]\" is substituted with r0b value,"+commonVariables.lineBreak+"                      * \"[ra]\" is substituted with r0a value,"+commonVariables.lineBreak+"                      * \"[ex]\" is substituted with experiment id,"+commonVariables.lineBreak+"                      * \"^\" is substituted with empty string.");
					performCalculations=false;
				} else if(str[i].equalsIgnoreCase("--outTxt") || str[i].equalsIgnoreCase("-ot")) {
					outTxt=true;
					useGraphicalInterface=false;
				} else if(str[i].equalsIgnoreCase("--experiment") || str[i].equalsIgnoreCase("-ex")) {
					i++;
					commonVariables.experiment=Long.parseLong(str[i]);
					commonVariables.experimentTime=false;
				}
			}
		} catch (Exception e) {
			System.out.println("Error in input!");
			System.out.println("-> "+str[i]+" <-");
			System.exit(0);
		}
	}
	public static void main(String [] args) {
		commonVariables.approx[0]=0.5;
		commonVariables.approx[1]=1.5;
		commonVariables.approx[2]=-6;
		commonVariables.approx[3]=-5;
		commonVariables.approx[4]=-3.5;
		commonVariables.approx[5]=-2;
		commonVariables.approx[6]=-4;
		commonVariables.approx[7]=-3;		
		getCommandLine(args);
		if(!performCalculations) return ;
		if(useGraphicalInterface) {
			mainWindow langas=new mainWindow();
		} else {
			sdeSolveOuter langas=new sdeSolveOuter(outTxt, graphicslFileOut, true, commandLine, noOut, approxDrop, null, approx);
		}
	}
}

/*main program window class*/
class mainWindow extends JFrame implements ActionListener {
	public JPanel contentPane=new JPanel();
	public plotComponent graph=new plotComponent();
	private JMenuBar mainMenu=new JMenuBar();
	public JMenu modelMenu=new JMenu("Model");
	private JMenuItem generate=new JMenuItem("Generate",KeyEvent.VK_G);
	private JMenuItem quit=new JMenuItem("Quit",KeyEvent.VK_Q);
	private JMenuItem modelSettings=new JMenuItem("Model related settings",KeyEvent.VK_M);
	private JMenuItem noiseSettings=new JMenuItem("Noise settings",KeyEvent.VK_N);
	private JMenuItem mixedSettings=new JMenuItem("Mixed settings",KeyEvent.VK_I);
	private JMenuItem saveSettingsToScript=new JMenuItem("Save settings to script",KeyEvent.VK_S);
	private JMenuItem saveSettingsToFile=new JMenuItem("Save settings to file",KeyEvent.VK_F);
	private JMenuItem softwareSettings=new JMenuItem("Software related settings",KeyEvent.VK_O);
	private JMenu spectraMenu=new JMenu("Spectra");
	private JMenuItem screenOut1=new JMenuItem("On screen (without approximation)",KeyEvent.VK_O);
	private JMenuItem approxPdf=new JMenuItem("On screen (with approximation)",KeyEvent.VK_A);
	private JMenuItem approxPdfSilent=new JMenuItem("On screen (with silent approximation)",KeyEvent.VK_L);
	private JMenuItem txtOut1=new JMenuItem("To txt",KeyEvent.VK_X);
	private JMenuItem svgOut1=new JMenuItem("To svg + on screen (as is)",KeyEvent.VK_S);
	private JMenuItem pngOut1=new JMenuItem("To png + on screen (as is)",KeyEvent.VK_P);
	private JMenu pdfMenu=new JMenu("PDF");
	private JMenuItem screenOut2=new JMenuItem("On screen (without approximation)",KeyEvent.VK_O);
	private JMenuItem approxSpec=new JMenuItem("On screen (with approximation)",KeyEvent.VK_A);
	private JMenuItem approxSpecSilent=new JMenuItem("On screen (with silent approximation)",KeyEvent.VK_L);
	private JMenuItem txtOut2=new JMenuItem("To txt",KeyEvent.VK_X);
	private JMenuItem svgOut2=new JMenuItem("To svg + on screen (as is)",KeyEvent.VK_S);
	private JMenuItem pngOut2=new JMenuItem("To png + on screen (as is)",KeyEvent.VK_P);
	public double[][] pdf=null;
	public double[][] spec=null;
	private sdeSolveOuter solving;
	public int whichOutput=-1;//-1 - no, 0 - spectra, 1 - pdf
	public int approxWho=-1;
	public double[] approxWhat=null;
	//-----------------------------
	public void saveSettingsToScript() {
		int mem=(int)(Runtime.getRuntime().maxMemory()/1024/1024);
		int exponent2=(int)(Math.log(mem)/Math.log(2));
		double exponent1=Math.log(mem)/Math.log(2);
		if(exponent2!=exponent1) {
			mem=(int)Math.pow(2,exponent2+1);
		}		
		String extension="sh";
		if(System.getProperty("os.name").indexOf("Windows")>-1) {
			extension="bat";
		}
		BufferedWriter out=null;
		String fileName="run."+extension;
		try {
			File file = new File(fileName);
			if(file.exists()) file.delete();
			file.createNewFile();
			out=new BufferedWriter(new FileWriter(fileName));
			out.write("@java -Xmx"+mem+"m -jar returnModel.jar");
			out.write(" --core "+commonVariables.core);
			out.write(" --realizations "+commonVariables.realizations);
			out.write(" --points "+commonVariables.realPoints);
			out.write(" --lambda "+commonVariables.lambda);
			out.write(" --epsilon "+commonVariables.epsilon);
			out.write(" --eta "+commonVariables.eta);
			out.write(" --kappa "+commonVariables.kappa);
			out.write(" --tau "+commonVariables.tau);
			out.write(" --tau60 "+commonVariables.tau60);
			out.write(" --deltaT "+commonVariables.deltaT);
			if(commonVariables.useLimits) {
				out.write(" --useLimits");
			}
			if(commonVariables.oldDiffusionLimiter && !commonVariables.simpleSDE) {
				out.write(" --oldLimiter");
			}
			if(commonVariables.useLimits||(!commonVariables.oldDiffusionLimiter)) out.write(" --xmax "+commonVariables.xmax);
			if(commonVariables.simpleSDE) out.write(" --simpleSDE");
			if(!commonVariables.noise) out.write(" --noNoise");
			else {
				out.write(" --lambda2 "+commonVariables.lambda2);
				out.write(" --r0b "+commonVariables.r0b);
				out.write(" --r0a "+commonVariables.r0a);
			}
			if(!commonVariables.minusMean) out.write(" --notMinusMean");
			out.write(" --pdfMax "+commonVariables.pdfMax);
			out.write(" --pdfMin "+commonVariables.pdfScale);
			out.write(" --approx "+commonVariables.approx[0]+" "+commonVariables.approx[1]+" "+commonVariables.approx[2]+" "+commonVariables.approx[3]+" "+commonVariables.approx[4]+" "+commonVariables.approx[5]+" "+commonVariables.approx[6]+" "+commonVariables.approx[7]);
			out.write(" --outPoints "+commonVariables.outPoints);
			out.write(" --output "+commonVariables.outputStr.replaceAll("\\^","\\^\\^"));
			out.write(" --window "+commonVariables.graphWidth+" "+commonVariables.graphHeight);
			if(commonVariables.windowCentered) out.write(" centered");
			else out.write(" notCentered");
			out.close();
		} catch (Exception e) {
			System.out.println("#002a Error while writing file "+fileName);
			e.printStackTrace();
		}
	}
	public void saveSettingsToFile() {
		BufferedWriter out=null;
		String fileName=System.currentTimeMillis()+".settings";
		try {
			File file = new File(fileName);
			if(file.exists()) file.delete();
			file.createNewFile();
			out=new BufferedWriter(new FileWriter(fileName));
			if(commonVariables.simpleSDE) out.write("simpleSDE is on"+commonVariables.lineBreak);
			else out.write("simpleSDE is off"+commonVariables.lineBreak);
			if(commonVariables.useLimits) {
				out.write("limits are on"+commonVariables.lineBreak);
			} else out.write("limits are off"+commonVariables.lineBreak);
			if(commonVariables.simpleSDE) {
				if(commonVariables.oldDiffusionLimiter) {
					out.write("Using old diffusion limiter"+commonVariables.lineBreak);
				} else {
					out.write("Using new diffusion limiter"+commonVariables.lineBreak);
				}
			}
			if(commonVariables.useLimits||(!commonVariables.oldDiffusionLimiter)) out.write("xmax="+commonVariables.xmax+commonVariables.lineBreak);
			out.write("lambda="+commonVariables.lambda+commonVariables.lineBreak);
			out.write("epsilon="+commonVariables.epsilon+commonVariables.lineBreak);
			out.write("eta="+commonVariables.eta+commonVariables.lineBreak);
			out.write("kappa="+commonVariables.kappa+commonVariables.lineBreak);
			out.write("tau="+commonVariables.tau+commonVariables.lineBreak);
			out.write("tau60="+commonVariables.tau60+commonVariables.lineBreak);
			out.write("deltaT="+commonVariables.deltaT+commonVariables.lineBreak);
			if(!commonVariables.noise) out.write("noise is not applied"+commonVariables.lineBreak);
			else out.write("noise is applied"+commonVariables.lineBreak);
			out.write("lambda2="+commonVariables.lambda2+commonVariables.lineBreak);
			out.write("r0b="+commonVariables.r0b+commonVariables.lineBreak);
			out.write("r0a="+commonVariables.r0a+commonVariables.lineBreak);
			out.write("approx[]={"+commonVariables.approx[0]+","+commonVariables.approx[1]+";"+commonVariables.approx[2]+","+commonVariables.approx[3]+","+commonVariables.approx[4]+","+commonVariables.approx[5]+";"+commonVariables.approx[6]+","+commonVariables.approx[7]+"}"+commonVariables.lineBreak);
			if(!commonVariables.minusMean) out.write("Mean is not subtracted before calculating PSD."+commonVariables.lineBreak);
			else out.write("Mean is subtracted before calculating PSD."+commonVariables.lineBreak);
			out.close();
		} catch (Exception e) {
			System.out.println("#002a Error while writing file "+fileName);
			e.printStackTrace();
		}
	}
	//main window class constructor
	public mainWindow() {
		//Contentpane
		contentPane.setPreferredSize(new Dimension(commonVariables.graphWidth,commonVariables.graphHeight));
		contentPane.setLayout(new BorderLayout());
		contentPane.add(graph,BorderLayout.CENTER);
		setContentPane(contentPane);
		//menubar
		modelMenu.add(generate);
		generate.addActionListener(this);
		generate.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_G, ActionEvent.CTRL_MASK));
		modelMenu.addSeparator();
		modelMenu.add(modelSettings);
		modelSettings.addActionListener(this);
		modelMenu.add(noiseSettings);
		noiseSettings.addActionListener(this);
		modelMenu.add(softwareSettings);
		softwareSettings.addActionListener(this);
		modelMenu.add(mixedSettings);
		mixedSettings.addActionListener(this);
		modelMenu.addSeparator();
		modelMenu.add(saveSettingsToScript);
		saveSettingsToScript.addActionListener(this);
		modelMenu.add(saveSettingsToFile);
		saveSettingsToFile.addActionListener(this);
		modelMenu.addSeparator();
		modelMenu.add(quit);
		quit.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Q, ActionEvent.CTRL_MASK));
		quit.addActionListener(this);
		spectraMenu.add(screenOut1);
		screenOut1.addActionListener(this);
		spectraMenu.add(approxSpec);
		approxSpec.addActionListener(this);
		spectraMenu.add(approxSpecSilent);
		approxSpecSilent.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, ActionEvent.CTRL_MASK));
		approxSpecSilent.addActionListener(this);
		spectraMenu.addSeparator();
		spectraMenu.add(svgOut1);
		svgOut1.addActionListener(this);
		spectraMenu.add(pngOut1);
		pngOut1.addActionListener(this);
		spectraMenu.addSeparator();
		spectraMenu.add(txtOut1);
		txtOut1.addActionListener(this);
		pdfMenu.add(screenOut2);
		screenOut2.addActionListener(this);
		pdfMenu.add(approxPdf);
		approxPdf.addActionListener(this);
		pdfMenu.add(approxPdfSilent);
		approxPdfSilent.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_P, ActionEvent.CTRL_MASK));
		approxPdfSilent.addActionListener(this);
		pdfMenu.addSeparator();
		pdfMenu.add(svgOut2);
		svgOut2.addActionListener(this);
		pdfMenu.add(pngOut2);
		pngOut2.addActionListener(this);
		pdfMenu.addSeparator();
		pdfMenu.add(txtOut2);
		txtOut2.addActionListener(this);
		mainMenu.add(modelMenu);
		modelMenu.setMnemonic(KeyEvent.VK_M);
		mainMenu.add(spectraMenu);
		spectraMenu.setMnemonic(KeyEvent.VK_S);
		mainMenu.add(pdfMenu);
		pdfMenu.setMnemonic(KeyEvent.VK_P);
		setJMenuBar(mainMenu);
		//further setup of window
		menuEnabled(false);
		modelMenu.setEnabled(true);
		pack();
		if(commonVariables.windowCentered) {
			setLocationRelativeTo(null);
		}
		setTitle("returnModel v101201");
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		//graph initialization
		graph.ini(commonVariables.graphWidth,commonVariables.graphHeight);
		graph.setLimits(-1,1,-1,1);
		graph.useGridlines(true);
		graph.clearPlot();
		graph.drawAxis();
		graph.renderPlot();
		//color scheme
		setBackground(Color.white);
		setForeground(Color.black);
		contentPane.setBackground(Color.white);
		contentPane.setForeground(Color.black);
		mainMenu.setBackground(Color.white);
		mainMenu.setForeground(Color.black);
		modelMenu.setBackground(Color.white);
		modelMenu.setForeground(Color.black);
		generate.setBackground(Color.white);
		generate.setForeground(Color.black);
		quit.setBackground(Color.white);
		quit.setForeground(Color.black);
		modelSettings.setBackground(Color.white);
		modelSettings.setForeground(Color.black);
		noiseSettings.setBackground(Color.white);
		noiseSettings.setForeground(Color.black);
		mixedSettings.setBackground(Color.white);
		mixedSettings.setForeground(Color.black);
		saveSettingsToScript.setBackground(Color.white);
		saveSettingsToScript.setForeground(Color.black);
		saveSettingsToFile.setBackground(Color.white);
		saveSettingsToFile.setForeground(Color.black);
		softwareSettings.setBackground(Color.white);
		softwareSettings.setForeground(Color.black);
		spectraMenu.setBackground(Color.white);
		spectraMenu.setForeground(Color.black);
		screenOut1.setBackground(Color.white);
		screenOut1.setForeground(Color.black);
		screenOut2.setBackground(Color.white);
		screenOut2.setForeground(Color.black);
		txtOut1.setBackground(Color.white);
		txtOut1.setForeground(Color.black);
		svgOut1.setBackground(Color.white);
		svgOut1.setForeground(Color.black);
		pngOut1.setBackground(Color.white);
		pngOut1.setForeground(Color.black);
		txtOut2.setBackground(Color.white);
		txtOut2.setForeground(Color.black);
		svgOut2.setBackground(Color.white);
		svgOut2.setForeground(Color.black);
		pngOut2.setBackground(Color.white);
		pngOut2.setForeground(Color.black);
		pdfMenu.setBackground(Color.white);
		pdfMenu.setForeground(Color.black);
		approxPdf.setBackground(Color.white);
		approxPdf.setForeground(Color.black);
		approxPdfSilent.setBackground(Color.white);
		approxPdfSilent.setForeground(Color.black);
		approxSpec.setBackground(Color.white);
		approxSpec.setForeground(Color.black);
		approxSpecSilent.setBackground(Color.white);
		approxSpecSilent.setForeground(Color.black);
		setVisible(true);
	}
	//function used to pass program results
	public void put(double[][] p, double[][]s) {
		if((p==null)&&(s==null)) {
			solving=null;
			graph.ini(commonVariables.graphWidth,commonVariables.graphHeight);
			graph.setLimits(-1,1,-1,1);
			graph.useGridlines(true);
			graph.clearPlot();
			graph.drawAxis();
			graph.renderPlot();
			return ;
		}
		pdf=new double[p.length][2];
		spec=new double[s.length][2];
		pdf=p;
		spec=s;
		menuEnabled(true);
		solving=null;
		//drawing spectra with approximation silently
		//approxWho=1;
		approxDraw(true);
	}
	//ploting to graph functions
	private void plotPdf() {
		plotPdf("");
	}
	private void plotPdf(String additionalLabel) {
		whichOutput=1;
		double xmin=Double.MAX_VALUE;
		double xmax=-Double.MAX_VALUE;
		double ymin=Double.MAX_VALUE;
		double ymax=-Double.MAX_VALUE;
		for(int i=0;i<pdf.length;i++) {
			xmin=Math.min(xmin,pdf[i][0]);
			xmax=Math.max(xmax,pdf[i][0]);
			ymin=Math.min(ymin,pdf[i][1]);
			ymax=Math.max(ymax,pdf[i][1]);
		}
		if(Math.ceil(ymin)-ymin>0.5) ymin=Math.ceil(ymin)-1;
		else ymin=Math.ceil(ymin)-0.5;
		if(Math.floor(ymax)-ymax<-0.5) ymax=Math.floor(ymax)+1;
		else ymax=Math.floor(ymax)+0.5;
		graph.setLimits(xmin,xmax,ymin,ymax);
		if(additionalLabel.equalsIgnoreCase("")) graph.setPlotLabel("PDF");
		else graph.setPlotLabel("PDF ("+additionalLabel+")");
		graph.useGridlines(true);
		graph.clearPlot();
		graph.drawAxis();
		graph.plotArray(pdf,Color.darkGray,true);
		graph.renderPlot();
	}
	private void plotSpectra() {
		plotSpectra("");
	}
	private void plotSpectra(String additionalLabel) {
		whichOutput=0;
		double xmin=Double.MAX_VALUE;
		double xmax=-Double.MAX_VALUE;
		double ymin=Double.MAX_VALUE;
		double ymax=-Double.MAX_VALUE;
		for(int i=0;i<spec.length;i++) {
			xmin=Math.min(xmin,spec[i][0]);
			xmax=Math.max(xmax,spec[i][0]);
			ymin=Math.min(ymin,spec[i][1]);
			ymax=Math.max(ymax,spec[i][1]);
		}
		if(Math.ceil(ymin)-ymin>0.5) ymin=Math.ceil(ymin)-1;
		else ymin=Math.ceil(ymin)-0.5;
		if(Math.floor(ymax)-ymax<-0.5) ymax=Math.floor(ymax)+1;
		else ymax=Math.floor(ymax)+0.5;
		graph.setLimits(xmin,xmax,ymin,ymax);
		if(additionalLabel.equalsIgnoreCase("")) graph.setPlotLabel("Power spectra density");
		else graph.setPlotLabel("Power spectra density ("+additionalLabel+")");
		graph.useGridlines(true);
		graph.clearPlot();
		graph.drawAxis();
		graph.plotArray(spec,Color.darkGray,true);
		graph.renderPlot();
	}
	//substitution in output template
	public String subOutTemp(String str) {
		String tmp=str.replaceAll("\\[l\\]",""+commonVariables.lambda);
		tmp=tmp.replaceAll("\\[e\\]",""+commonVariables.epsilon);
		tmp=tmp.replaceAll("\\[n\\]",""+commonVariables.eta);
		tmp=tmp.replaceAll("\\[k\\]",""+commonVariables.kappa);
		tmp=tmp.replaceAll("\\[t\\]",""+commonVariables.tau);
		tmp=tmp.replaceAll("\\[l2\\]",""+commonVariables.lambda2);
		tmp=tmp.replaceAll("\\[rb\\]",""+commonVariables.r0b);
		tmp=tmp.replaceAll("\\[ra\\]",""+commonVariables.r0a);
		tmp=tmp.replaceAll("\\[ex\\]",""+commonVariables.experiment);
		tmp=tmp.replaceAll("\\[xmx\\]",""+commonVariables.xmax);
		tmp=tmp.replaceAll("\\^","");
		return tmp;
	}
	//event handler
	public void actionPerformed(ActionEvent e) {
		if(e.getSource()==quit) {
			System.exit(0);
		} else if(e.getSource()==generate) {			
			menuEnabled(false);
			if(commonVariables.experimentTime) commonVariables.experiment=System.currentTimeMillis();
			solving=new sdeSolveOuter(false, -1, false, false, false, false, this, false);
		} else if(e.getSource()==screenOut1) {
			if(whichOutput!=0) plotSpectra();
		} else if(e.getSource()==screenOut2) {
			if(whichOutput!=1) plotPdf();
		} else if(e.getSource()==pngOut1) {
			if(whichOutput!=0) plotSpectra();
			String tmp=subOutTemp(commonVariables.outputStr);
			graph.save(tmp+"[spec].png");
		} else if(e.getSource()==pngOut2) {
			if(whichOutput!=1) plotPdf();
			String tmp=subOutTemp(commonVariables.outputStr);
			graph.save(tmp+"[pdf].png");
		} else if(e.getSource()==svgOut1) {
			if(whichOutput!=0) plotSpectra();
			String tmp=subOutTemp(commonVariables.outputStr);
			graph.save(tmp+"[spec].svg");
		} else if(e.getSource()==svgOut2) {
			if(whichOutput!=1) plotPdf();
			String tmp=subOutTemp(commonVariables.outputStr);
			graph.save(tmp+"[pdf].svg");
		} else if(e.getSource()==txtOut1) {
			String tmp=subOutTemp(commonVariables.outputStr);
			outputarr(spec,tmp+".spec");
		} else if(e.getSource()==txtOut2) {
			String tmp=subOutTemp(commonVariables.outputStr);
			outputarr(pdf,tmp+".dist");
		} else if(e.getSource()==modelSettings) {
			menuEnabled(false);
			new modelSW(this);
		} else if(e.getSource()==saveSettingsToScript) {
			saveSettingsToScript();
		} else if(e.getSource()==saveSettingsToFile) {
			saveSettingsToFile();
		} else if(e.getSource()==mixedSettings) {
			menuEnabled(false);
			new mixedSW(this);
		} else if(e.getSource()==noiseSettings) {
			menuEnabled(false);
			new noiseSW(this);
		} else if(e.getSource()==softwareSettings) {
			menuEnabled(false);
			new softSW(this);
		} else if(e.getSource()==approxPdf) {
			menuEnabled(false);
			approxWho=0;
			new approxSW(this);
		} else if(e.getSource()==approxSpec) {
			menuEnabled(false);
			approxWho=1;
			new approxSW(this);
		} else if(e.getSource()==approxPdfSilent) {
			approxWho=0;
			approxDraw(true);
		} else if(e.getSource()==approxSpecSilent) {
			approxWho=1;
			approxDraw(true);
		}
	}
	//enable or disable menus
	public void menuEnabled(boolean u) {
		modelMenu.setEnabled(u);
		pdfMenu.setEnabled(u);
		spectraMenu.setEnabled(u);
	}
	//draw functions which approximate graph
	public void approxDraw() {
		approxDraw(false);
	}
	public void approxDraw(boolean silent) {
		double[] tmp=null;
		double[] tmp2=null;
		if(approxWho==0) {
			if(silent) {
				approxWhat=new double[2];
				approxWhat[0]=commonVariables.approx[0];
				approxWhat[1]=commonVariables.approx[1];
			}
			tmp=commonFunctions.approxim(pdf,approxWhat[0],approxWhat[1]);
			String pap=commonFunctions.round(tmp[0],3)+"*x";
			if(commonFunctions.round(tmp[1],3)>=0) pap+="+"+commonFunctions.round(tmp[1],3);
			else pap+=commonFunctions.round(tmp[1],3);
			plotPdf(pap);
		} else {
			if(!commonVariables.simpleSDE) {
				if(silent) {
					approxWhat=new double[4];
					approxWhat[0]=commonVariables.approx[2];
					approxWhat[1]=commonVariables.approx[3];
					approxWhat[2]=commonVariables.approx[4];
					approxWhat[3]=commonVariables.approx[5];
				}
				tmp=commonFunctions.approxim(spec,approxWhat[0],approxWhat[1]);
				tmp2=commonFunctions.approxim(spec,approxWhat[2],approxWhat[3]);
				String pap=commonFunctions.round(tmp[0],3)+"*x";
				if(commonFunctions.round(tmp[1],3)>=0) pap+="+"+commonFunctions.round(tmp[1],3);
				else pap+=commonFunctions.round(tmp[1],3);
				pap+=", "+commonFunctions.round(tmp2[0],3)+"*x";
				if(commonFunctions.round(tmp2[1],3)>=0) pap+="+"+commonFunctions.round(tmp2[1],3);
				else pap+=commonFunctions.round(tmp2[1],3);
				plotSpectra(pap);
			} else {
				if(silent) {
					approxWhat=new double[2];
					approxWhat[0]=commonVariables.approx[6];
					approxWhat[1]=commonVariables.approx[7];
				}
				tmp=commonFunctions.approxim(spec,approxWhat[0],approxWhat[1]);
				String pap=commonFunctions.round(tmp[0],3)+"*x";
				if(commonFunctions.round(tmp[1],3)>=0) pap+="+"+commonFunctions.round(tmp[1],3);
				else pap+=commonFunctions.round(tmp[1],3);
				plotSpectra(pap);
			}
		}
		double[][] outarr=new double[commonVariables.outPoints+1][2];
		double[][] outarr2=null;
		if(tmp2!=null) outarr2=new double[commonVariables.outPoints+1][2];
		double zingsnis=(graph.xMax-graph.xMin)/((double)commonVariables.outPoints);
		for(int i=0;i<outarr.length;i++) {
			outarr[i][0]=graph.xMin+i*zingsnis;
			outarr[i][1]=tmp[0]*outarr[i][0]+tmp[1];
			if(tmp2!=null) {
				outarr2[i][0]=graph.xMin+i*zingsnis;
				outarr2[i][1]=tmp2[0]*outarr2[i][0]+tmp2[1];
			}
		}
		graph.plotArray(outarr,Color.red,true);
		if(outarr2!=null) graph.plotArray(outarr2,Color.red,true);
		graph.renderPlot();
	}
	//output to text file
	public void outputarr(double[][] arr, String name) {
		BufferedWriter out=null;
		String fileName=name;
		try {
			File file = new File(fileName);
			file.createNewFile();
			out = new BufferedWriter(new FileWriter(fileName));
			for(int i=0;i<arr.length;i++) {
				out.write(commonFunctions.round(arr[i][0],6)+" "+commonFunctions.round(arr[i][1],6)+""+commonVariables.lineBreak+"");
			} 
			out.close();
		} catch (Exception e) {
			System.out.println("#002a Error while writing file "+fileName);
			e.printStackTrace();
		}
	}
}

//model settings window
class modelSW extends JFrame implements ActionListener{
	private mainWindow parrent;
	private JPanel centr=new JPanel();
	private JLabel lambdaLab=new JLabel("lambda=");
	private JLabel epsilonLab=new JLabel("epsilon=");
	private JLabel etaLab=new JLabel("eta=");
	private JLabel kappaLab=new JLabel("kappa=");
	private JLabel tauLab=new JLabel("tau=");
	private JLabel tauLab60=new JLabel("tau60=");
	private JLabel deltaTLab=new JLabel("dT=");
	private JLabel xmaxLab=new JLabel("xmax=");
	private JTextField lambdaTf;
	private JTextField epsilonTf;
	private JTextField etaTf;
	private JTextField kappaTf;
	private JTextField tauTf;
	private JTextField tauTf60;
	private JTextField deltaTTf;
	private JTextField xmaxTf;
	private JCheckBox useLimitsCheck=new JCheckBox("Use limits?");
	private JCheckBox oldLimiterCheck=new JCheckBox("Use old diffusion limiter?");
	private JCheckBox simpleSDECheck=new JCheckBox("Simple SDE?");
	private JButton OKButton=new JButton("Confirm");
	//constructor
	public modelSW(mainWindow par) {
		parrent=par;
		//preseting default values
		lambdaTf=new JTextField(""+commonVariables.lambda);
		epsilonTf=new JTextField(""+commonVariables.epsilon);
		etaTf=new JTextField(""+commonVariables.eta);
		kappaTf=new JTextField(""+commonVariables.kappa);
		tauTf=new JTextField(""+commonVariables.tau);
		tauTf60=new JTextField(""+commonVariables.tau60);
		deltaTTf=new JTextField(""+commonVariables.deltaT);
		xmaxTf=new JTextField(""+commonVariables.xmax);
		useLimitsCheck.setSelected(commonVariables.useLimits);
		oldLimiterCheck.setSelected(commonVariables.oldDiffusionLimiter);
		simpleSDECheck.setSelected(commonVariables.simpleSDE);
		//making GUI
		Container contentPane=this.getContentPane();
		contentPane.setLayout(new BorderLayout());
		contentPane.add(centr,BorderLayout.CENTER);
		centr.setPreferredSize(new Dimension(230,370));
		pack();
		lambdaLab.setBounds(10,10,100,20);lambdaTf.setBounds(120,10,100,20);
		epsilonLab.setBounds(10,40,100,20);epsilonTf.setBounds(120,40,100,20);
		etaLab.setBounds(10,70,100,20);etaTf.setBounds(120,70,100,20);
		kappaLab.setBounds(10,100,100,20);kappaTf.setBounds(120,100,100,20);
		tauLab.setBounds(10,130,100,20);tauTf.setBounds(120,130,100,20);
		tauLab60.setBounds(10,160,100,20);tauTf60.setBounds(120,160,100,20);
		deltaTLab.setBounds(10,190,100,20);deltaTTf.setBounds(120,190,100,20);
		useLimitsCheck.setBounds(10,220,210,20);
		oldLimiterCheck.setBounds(10,250,210,20);
		xmaxLab.setBounds(10,280,100,20);xmaxTf.setBounds(120,280,100,20);
		simpleSDECheck.setBounds(10,310,210,20);
		OKButton.setBounds(10,340,210,20);
		OKButton.addActionListener(this);
		centr.setLayout(null);
		centr.add(lambdaLab);centr.add(lambdaTf);
		centr.add(epsilonLab);centr.add(epsilonTf);
		centr.add(etaLab);centr.add(etaTf);
		centr.add(kappaLab);centr.add(kappaTf);
		centr.add(tauLab);centr.add(tauTf);
		centr.add(tauLab60);centr.add(tauTf60);
		centr.add(deltaTLab);centr.add(deltaTTf);
		centr.add(xmaxLab);centr.add(xmaxTf);
		centr.add(useLimitsCheck);
		centr.add(oldLimiterCheck);
		centr.add(simpleSDECheck);
		centr.add(OKButton);
		setLocationRelativeTo(par);
		setResizable(false);
		setTitle("Model settings");
		setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		//Color scheme
		setBackground(Color.white);
		setForeground(Color.black);
		epsilonLab.setBackground(Color.white);
		epsilonLab.setForeground(Color.black);
		etaLab.setBackground(Color.white);
		etaLab.setForeground(Color.black);
		kappaLab.setBackground(Color.white);
		kappaLab.setForeground(Color.black);
		tauLab.setBackground(Color.white);
		tauLab.setForeground(Color.black);
		tauLab60.setBackground(Color.white);
		tauLab60.setForeground(Color.black);
		deltaTLab.setBackground(Color.white);
		deltaTLab.setForeground(Color.black);
		xmaxLab.setBackground(Color.white);
		xmaxLab.setForeground(Color.black);
		lambdaLab.setBackground(Color.white);
		lambdaLab.setForeground(Color.black);
		epsilonTf.setBackground(Color.white);
		epsilonTf.setForeground(Color.black);
		etaTf.setBackground(Color.white);
		etaTf.setForeground(Color.black);
		kappaTf.setBackground(Color.white);
		kappaTf.setForeground(Color.black);
		tauTf.setBackground(Color.white);
		tauTf.setForeground(Color.black);
		tauTf60.setBackground(Color.white);
		tauTf60.setForeground(Color.black);
		deltaTTf.setBackground(Color.white);
		deltaTTf.setForeground(Color.black);
		xmaxTf.setBackground(Color.white);
		xmaxTf.setForeground(Color.black);
		lambdaTf.setBackground(Color.white);
		lambdaTf.setForeground(Color.black);
		centr.setBackground(Color.white);
		centr.setForeground(Color.black);
		useLimitsCheck.setBackground(Color.white);
		oldLimiterCheck.setBackground(Color.white);
		useLimitsCheck.setForeground(Color.black);
		oldLimiterCheck.setForeground(Color.black);
		simpleSDECheck.setBackground(Color.white);
		simpleSDECheck.setForeground(Color.black);
		OKButton.setBackground(Color.white);
		OKButton.setForeground(Color.black);
		//---
		setVisible(true);
	}
	//event handler
	public void actionPerformed(ActionEvent e) {
		if(e.getSource()==OKButton) {
			commonVariables.lambda=Double.parseDouble(lambdaTf.getText());
			commonVariables.epsilon=Double.parseDouble(epsilonTf.getText());
			commonVariables.eta=Double.parseDouble(etaTf.getText());
			commonVariables.kappa=Double.parseDouble(kappaTf.getText());
			commonVariables.tau=Double.parseDouble(tauTf.getText());
			commonVariables.tau60=Double.parseDouble(tauTf60.getText());
			commonVariables.deltaT=Double.parseDouble(deltaTTf.getText());
			commonVariables.xmax=Double.parseDouble(xmaxTf.getText());
			commonVariables.useLimits=useLimitsCheck.isSelected();
			commonVariables.oldDiffusionLimiter=oldLimiterCheck.isSelected();
			commonVariables.simpleSDE=simpleSDECheck.isSelected();
			if(parrent.pdf!=null) {
				parrent.menuEnabled(true);
			} else {
				parrent.modelMenu.setEnabled(true);
			}
			setVisible(false);
			dispose();
		}
	}
}

//model settings window
class noiseSW extends JFrame implements ActionListener{
	private mainWindow parrent;
	private JPanel centr=new JPanel();
	private JLabel lambda2Lab=new JLabel("lambda2=");
	private JLabel r0bLab=new JLabel("r0b=");
	private JLabel r0aLab=new JLabel("r0a=");
	private JTextField lambda2Tf;
	private JTextField r0bTf;
	private JTextField r0aTf;
	private JCheckBox useNoise=new JCheckBox("Use noise?");
	private JButton OKButton=new JButton("Confirm");
	//constructor
	public noiseSW(mainWindow par) {
		parrent=par;
		//preseting default values
		lambda2Tf=new JTextField(""+commonVariables.lambda2);
		r0bTf=new JTextField(""+commonVariables.r0b);
		r0aTf=new JTextField(""+commonVariables.r0a);
		useNoise.setSelected(commonVariables.noise);
		//making GUI
		Container contentPane=this.getContentPane();
		contentPane.setLayout(new BorderLayout());
		contentPane.add(centr,BorderLayout.CENTER);
		centr.setPreferredSize(new Dimension(230,160));
		pack();
		useNoise.setBounds(10,10,210,20);
		lambda2Lab.setBounds(10,40,100,20);lambda2Tf.setBounds(120,40,100,20);
		r0bLab.setBounds(10,70,100,20);r0bTf.setBounds(120,70,100,20);
		r0aLab.setBounds(10,100,100,20);r0aTf.setBounds(120,100,100,20);
		OKButton.setBounds(10,130,210,20);
		OKButton.addActionListener(this);
		centr.setLayout(null);
		centr.add(useNoise);
		centr.add(lambda2Lab);centr.add(lambda2Tf);
		centr.add(r0bLab);centr.add(r0bTf);
		centr.add(r0aLab);centr.add(r0aTf);
		centr.add(OKButton);
		setLocationRelativeTo(par);
		setResizable(false);
		setTitle("Noise settings");
		setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		//Color scheme
		setBackground(Color.white);
		setForeground(Color.black);
		r0aLab.setBackground(Color.white);
		r0aLab.setForeground(Color.black);
		r0bLab.setBackground(Color.white);
		r0bLab.setForeground(Color.black);
		lambda2Lab.setBackground(Color.white);
		lambda2Lab.setForeground(Color.black);
		r0aTf.setBackground(Color.white);
		r0aTf.setForeground(Color.black);
		r0bTf.setBackground(Color.white);
		r0bTf.setForeground(Color.black);
		lambda2Tf.setBackground(Color.white);
		lambda2Tf.setForeground(Color.black);
		centr.setBackground(Color.white);
		centr.setForeground(Color.black);
		useNoise.setBackground(Color.white);
		useNoise.setForeground(Color.black);
		OKButton.setBackground(Color.white);
		OKButton.setForeground(Color.black);
		//---
		setVisible(true);
	}
	//event handler
	public void actionPerformed(ActionEvent e) {
		if(e.getSource()==OKButton) {
			commonVariables.lambda2=Double.parseDouble(lambda2Tf.getText());
			commonVariables.r0b=Double.parseDouble(r0bTf.getText());
			commonVariables.r0a=Double.parseDouble(r0aTf.getText());
			commonVariables.noise=useNoise.isSelected();
			if(parrent.pdf!=null) {
				parrent.menuEnabled(true);
			} else {
				parrent.modelMenu.setEnabled(true);
			}
			setVisible(false);
			dispose();
		}
	}
}

//mixed settings window
class mixedSW extends JFrame implements ActionListener{
	private mainWindow parrent;
	private JPanel centr=new JPanel();
	private JLabel coreLab=new JLabel("core=");
	private JLabel realizationsLab=new JLabel("realizations=");
	private JLabel realPointsLab=new JLabel("points[real]=");
	private JLabel pdfMaxLab=new JLabel("max[pdf]=");
	private JLabel pdfMinLab=new JLabel("min[pdf]=");
	private JLabel outPointsLab=new JLabel("points[out]=");
	private JTextField coreTf;
	private JTextField realizationsTf;
	private JTextField realPointsTf;
	private JTextField pdfMaxTf;
	private JTextField pdfMinTf;
	private JTextField outPointsTf;
	private JCheckBox minusMeanCheck=new JCheckBox("Subtract mean pre-FFT?");
	private JButton OKButton=new JButton("Confirm");
	//constructor
	public mixedSW(mainWindow par) {
		parrent=par;
		//preseting default values
		coreTf=new JTextField(""+commonVariables.core);
		realizationsTf=new JTextField(""+commonVariables.realizations);
		realPointsTf=new JTextField(""+commonVariables.realPoints);
		outPointsTf=new JTextField(""+commonVariables.outPoints);
		pdfMaxTf=new JTextField(""+commonVariables.pdfMax);
		pdfMinTf=new JTextField(""+commonVariables.pdfScale);
		minusMeanCheck.setSelected(commonVariables.minusMean);
		//making GUI
		Container contentPane=this.getContentPane();
		contentPane.setLayout(new BorderLayout());
		contentPane.add(centr,BorderLayout.CENTER);
		centr.setPreferredSize(new Dimension(230,250));
		pack();
		coreLab.setBounds(10,10,100,20);coreTf.setBounds(120,10,100,20);
		realizationsLab.setBounds(10,40,100,20);realizationsTf.setBounds(120,40,100,20);
		realPointsLab.setBounds(10,70,100,20);realPointsTf.setBounds(120,70,100,20);
		pdfMaxLab.setBounds(10,100,100,20);pdfMaxTf.setBounds(120,100,100,20);
		pdfMinLab.setBounds(10,130,100,20);pdfMinTf.setBounds(120,130,100,20);
		outPointsLab.setBounds(10,160,100,20);outPointsTf.setBounds(120,160,100,20);
		minusMeanCheck.setBounds(10,190,210,20);
		OKButton.setBounds(10,220,210,20);
		OKButton.addActionListener(this);
		centr.setLayout(null);
		centr.add(coreLab);centr.add(coreTf);
		centr.add(realizationsLab);centr.add(realizationsTf);
		centr.add(realPointsLab);centr.add(realPointsTf);
		centr.add(pdfMaxLab);centr.add(pdfMaxTf);
		centr.add(pdfMinLab);centr.add(pdfMinTf);
		centr.add(outPointsLab);centr.add(outPointsTf);
		centr.add(minusMeanCheck);
		centr.add(OKButton);
		setLocationRelativeTo(par);
		setResizable(false);
		setTitle("Mixed settings");
		setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		//color scheme
		setBackground(Color.white);
		setForeground(Color.black);
		coreLab.setBackground(Color.white);
		coreLab.setForeground(Color.black);
		realizationsLab.setBackground(Color.white);
		realizationsLab.setForeground(Color.black);
		realPointsLab.setBackground(Color.white);
		realPointsLab.setForeground(Color.black);
		pdfMaxLab.setBackground(Color.white);
		pdfMaxLab.setForeground(Color.black);
		pdfMinLab.setBackground(Color.white);
		pdfMinLab.setForeground(Color.black);
		outPointsLab.setBackground(Color.white);
		outPointsLab.setForeground(Color.black);
		coreTf.setBackground(Color.white);
		coreTf.setForeground(Color.black);
		realizationsTf.setBackground(Color.white);
		realizationsTf.setForeground(Color.black);
		realPointsTf.setBackground(Color.white);
		realPointsTf.setForeground(Color.black);
		pdfMaxTf.setBackground(Color.white);
		pdfMaxTf.setForeground(Color.black);
		pdfMinTf.setBackground(Color.white);
		pdfMinTf.setForeground(Color.black);
		outPointsTf.setBackground(Color.white);
		outPointsTf.setForeground(Color.black);
		centr.setBackground(Color.white);
		centr.setForeground(Color.black);
		minusMeanCheck.setBackground(Color.white);
		minusMeanCheck.setForeground(Color.black);
		OKButton.setBackground(Color.white);
		OKButton.setForeground(Color.black);
		//----
		setVisible(true);
	}
	//event listener
	public void actionPerformed(ActionEvent e) {
		if(e.getSource()==OKButton) {
			commonVariables.core=Integer.parseInt(coreTf.getText());
			commonVariables.realizations=Integer.parseInt(realizationsTf.getText());
			commonVariables.realPoints=Integer.parseInt(realPointsTf.getText());
			commonVariables.outPoints=Integer.parseInt(outPointsTf.getText());
			commonVariables.pdfMax=Double.parseDouble(pdfMaxTf.getText());
			commonVariables.pdfScale=Double.parseDouble(pdfMinTf.getText());
			commonVariables.minusMean=minusMeanCheck.isSelected();
			if(parrent.pdf!=null) {
				parrent.menuEnabled(true);
			} else {
				parrent.modelMenu.setEnabled(true);
			}
			setVisible(false);
			dispose();
		}
	}
}

//software settings window
class softSW extends JFrame implements ActionListener{
	private mainWindow parrent;
	private JPanel centr=new JPanel();
	private JLabel windowWidthLab=new JLabel("width[win]=");
	private JLabel windowHeightLab=new JLabel("height[win]=");
	private JLabel outputLab=new JLabel("template[out]=");
	private JTextField windowWidthTf;
	private JTextField windowHeightTf;
	private JTextField outputTf;
	private JButton OKButton=new JButton("Confirm");
	//constructor
	public softSW(mainWindow par) {
		parrent=par;
		//preseting default values
		windowWidthTf=new JTextField(""+commonVariables.graphWidth);
		windowHeightTf=new JTextField(""+commonVariables.graphHeight);
		outputTf=new JTextField(""+commonVariables.outputStr);
		//making GUI
		Container contentPane=this.getContentPane();
		contentPane.setLayout(new BorderLayout());
		contentPane.add(centr,BorderLayout.CENTER);
		centr.setPreferredSize(new Dimension(230,130));
		pack();
		windowWidthLab.setBounds(10,10,100,20);windowWidthTf.setBounds(120,10,100,20);
		windowHeightLab.setBounds(10,40,100,20);windowHeightTf.setBounds(120,40,100,20);
		outputLab.setBounds(10,70,100,20);outputTf.setBounds(120,70,100,20);
		OKButton.setBounds(10,100,210,20);
		OKButton.addActionListener(this);
		centr.setLayout(null);
		centr.add(windowWidthLab);centr.add(windowWidthTf);
		centr.add(windowHeightLab);centr.add(windowHeightTf);
		centr.add(outputLab);centr.add(outputTf);
		centr.add(OKButton);
		outputLab.setForeground(Color.lightGray);
		outputTf.setForeground(Color.lightGray);
		outputTf.setEditable(false);
		setLocationRelativeTo(par);
		setResizable(false);
		setTitle("Software settings");
		setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		//color scheme
		setBackground(Color.white);
		setForeground(Color.black);
		windowWidthLab.setBackground(Color.white);
		windowWidthLab.setForeground(Color.black);
		windowHeightLab.setBackground(Color.white);
		windowHeightLab.setForeground(Color.black);
		outputLab.setBackground(Color.white);
		outputLab.setForeground(Color.black);
		windowWidthTf.setBackground(Color.white);
		windowWidthTf.setForeground(Color.black);
		windowHeightTf.setBackground(Color.white);
		windowHeightTf.setForeground(Color.black);
		outputTf.setBackground(Color.white);
		outputTf.setForeground(Color.black);
		centr.setBackground(Color.white);
		centr.setForeground(Color.black);
		OKButton.setBackground(Color.white);
		OKButton.setForeground(Color.black);
		//-----
		setVisible(true);
	}
	//event handler
	public void actionPerformed(ActionEvent e) {
		if(e.getSource()==OKButton) {
			commonVariables.graphWidth=Integer.parseInt(windowWidthTf.getText());
			commonVariables.graphHeight=Integer.parseInt(windowHeightTf.getText());
			commonVariables.outputStr=outputTf.getText();
			parrent.contentPane.setPreferredSize(new Dimension(commonVariables.graphWidth,commonVariables.graphHeight));
			parrent.pack();
			parrent.graph.ini(commonVariables.graphWidth,commonVariables.graphHeight);
			parrent.graph.disablePlotLabel();
			parrent.graph.setLimits(-1,1,-1,1);
			parrent.graph.useGridlines(true);
			parrent.graph.clearPlot();
			parrent.graph.drawAxis();
			parrent.graph.renderPlot();
			//parrent.whichOutput=-1;
			if(parrent.pdf!=null) {
				parrent.menuEnabled(true);
			} else {
				parrent.modelMenu.setEnabled(true);
			}
			setVisible(false);
			dispose();
		}
	}
}

//approximation settings window
class approxSW extends JFrame implements ActionListener{
	private mainWindow parrent;
	private JPanel centr=new JPanel();
	private JLabel fromLab=new JLabel("lg(from)=");
	private JLabel toLab=new JLabel("lg(to)=");
	private JLabel from2Lab=new JLabel("lg(from[2])=");
	private JLabel to2Lab=new JLabel("lg(to[2])=");
	private JTextField fromTf;
	private JTextField toTf;
	private JTextField from2Tf;
	private JTextField to2Tf;
	private JButton OKButton=new JButton("Confirm");
	private boolean useFour=false;
	public approxSW(mainWindow par) {
		parrent=par;
		useFour=(parrent.approxWho==1)&&(!commonVariables.simpleSDE);
		//presseting default values
		if(parrent.approxWho==0){
			fromTf=new JTextField(commonVariables.approx[0]+"");
			toTf=new JTextField(commonVariables.approx[1]+"");
		} else if(parrent.approxWho==1){
			fromTf=new JTextField(commonVariables.approx[6]+"");
			toTf=new JTextField(commonVariables.approx[7]+"");
			if(useFour) {
				fromTf=new JTextField(commonVariables.approx[2]+"");
				toTf=new JTextField(commonVariables.approx[3]+"");
				fromLab.setText("lg(from[1])=");
				toLab.setText("lg(to[1])=");
			}
			from2Tf=new JTextField(commonVariables.approx[4]+"");
			to2Tf=new JTextField(commonVariables.approx[5]+"");
		}
		//making GUI
		Container contentPane=this.getContentPane();
		contentPane.setLayout(new BorderLayout());
		contentPane.add(centr,BorderLayout.CENTER);
		if(!useFour) centr.setPreferredSize(new Dimension(230,100));
		else centr.setPreferredSize(new Dimension(230,160));
		pack();
		fromLab.setBounds(10,10,100,20);fromTf.setBounds(120,10,100,20);
		toLab.setBounds(10,40,100,20);toTf.setBounds(120,40,100,20);
		if(!useFour) OKButton.setBounds(10,70,210,20);
		else {
			from2Lab.setBounds(10,70,100,20);from2Tf.setBounds(120,70,100,20);
			to2Lab.setBounds(10,100,100,20);to2Tf.setBounds(120,100,100,20);
			OKButton.setBounds(10,130,210,20);
		}
		OKButton.addActionListener(this);
		centr.setLayout(null);
		centr.add(fromLab);centr.add(fromTf);
		centr.add(toLab);centr.add(toTf);
		if(useFour) {
			centr.add(from2Lab);centr.add(from2Tf);
			centr.add(to2Lab);centr.add(to2Tf);
		}
		centr.add(OKButton);
		setLocationRelativeTo(par);
		setResizable(false);
		setTitle("Approximation settings");
		setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		//color scheme
		setBackground(Color.white);
		setForeground(Color.black);
		fromLab.setForeground(Color.black);
		fromLab.setBackground(Color.white);
		toLab.setBackground(Color.white);
		toLab.setForeground(Color.black);
		from2Lab.setForeground(Color.black);
		from2Lab.setBackground(Color.white);
		to2Lab.setBackground(Color.white);
		to2Lab.setForeground(Color.black);
		fromTf.setForeground(Color.black);
		fromTf.setBackground(Color.white);
		toTf.setBackground(Color.white);
		toTf.setForeground(Color.black);
		if(useFour) {
			from2Tf.setForeground(Color.black);
			from2Tf.setBackground(Color.white);
			to2Tf.setBackground(Color.white);
			to2Tf.setForeground(Color.black);
		}
		centr.setBackground(Color.white);
		centr.setForeground(Color.black);
		OKButton.setBackground(Color.white);
		OKButton.setForeground(Color.black);
		//----
		setVisible(true);
	}
	//event listener
	public void actionPerformed(ActionEvent e) {
		if(e.getSource()==OKButton) {
			if((!useFour)||(parrent.approxWho==0)) {
				parrent.approxWhat=new double[2];
				parrent.approxWhat[0]=Double.parseDouble(fromTf.getText());
				parrent.approxWhat[1]=Double.parseDouble(toTf.getText());
				if(parrent.approxWho==0) {
					commonVariables.approx[0]=Double.parseDouble(fromTf.getText());
					commonVariables.approx[1]=Double.parseDouble(toTf.getText());
				} else {
					commonVariables.approx[6]=Double.parseDouble(fromTf.getText());
					commonVariables.approx[7]=Double.parseDouble(toTf.getText());
				}
			} else {
				parrent.approxWhat=new double[4];
				parrent.approxWhat[0]=Double.parseDouble(fromTf.getText());
				parrent.approxWhat[1]=Double.parseDouble(toTf.getText());
				parrent.approxWhat[2]=Double.parseDouble(from2Tf.getText());
				parrent.approxWhat[3]=Double.parseDouble(to2Tf.getText());
				commonVariables.approx[2]=Double.parseDouble(fromTf.getText());
				commonVariables.approx[3]=Double.parseDouble(toTf.getText());
				commonVariables.approx[4]=Double.parseDouble(from2Tf.getText());
				commonVariables.approx[5]=Double.parseDouble(to2Tf.getText());
			}
			parrent.approxDraw();
			parrent.menuEnabled(true);
			setVisible(false);
			dispose();
		}
	}
}

//sde solve outer class
//basicly thread launcher and progress meter
class sdeSolveOuter extends JFrame {
	private boolean canceled=false;
	public double matlog2=Math.log(2);
	private JPanel centr=new JPanel();
	private int[] proc;
	private int tproc=0;
	private int dtproc=0;
	private int plotisPx=300;
	private double[] pdf;
	private double[] spec;
	private int baige=0;
	private Timer test;
	private boolean output=false;
	private int outputGraph=-1;
	private boolean terminateAfter=false;
	private double logStep;
	private int strwidth;
	private int strheight;
	private boolean commandLine;
	private boolean noOut;
	private mainWindow parrent;
	private boolean approxDrop=false;
	private boolean makeApprox=true;
	private sdeSolveInner[] gijos=null;
	//constructor
	public sdeSolveOuter(boolean is, int gis, boolean bd, boolean cl, boolean no, boolean appd, mainWindow par, boolean a) {
		checkRealPoints();
		//function parameters explained
		output=is;
		outputGraph=gis;
		terminateAfter=bd;
		commandLine=cl;
		noOut=no;
		approxDrop=appd;
		parrent=par;
		makeApprox=a;
		//---
		logStep=(commonFunctions.LogBase10((double)commonVariables.pdfMax)-commonFunctions.LogBase10(commonVariables.pdfScale))/((double)commonVariables.outPoints);
		pdf=new double[commonVariables.outPoints+1];
		for(int i=0;i<pdf.length;i++) pdf[i]=0;
		spec=new double[commonVariables.realPoints];
		for(int i=0;i<spec.length;i++) spec[i]=0;
		//making GUI
		Container contentPane=this.getContentPane();
		contentPane.setLayout(new BorderLayout());
		contentPane.add(centr, BorderLayout.CENTER);
		centr.setPreferredSize(new Dimension(plotisPx+20,55));
		pack();
		if(!commandLine) {
			setLocationRelativeTo(par);
			setResizable(false);
			setTitle("Calculating... | T:"+commonVariables.core+" | R: "+commonVariables.realizations+" |");
			setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
			setVisible(true);
			Graphics g=centr.getGraphics();
			FontMetrics fontMetrics=g.getFontMetrics();
			strwidth=fontMetrics.stringWidth("100%");
			strheight=fontMetrics.getHeight();
		}
		ActionListener taskPerformer=new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				if(!commandLine) repaint();
				else reportTerm();
			}
		};
		if(!commandLine) test=new Timer(50, taskPerformer);
		else test=new Timer(100, taskPerformer);
		if(!noOut) test.start();
		int tmp=commonVariables.realizations/commonVariables.core;
		int mod=commonVariables.realizations%commonVariables.core;
		proc=new int[commonVariables.core];
		//executing program
		gijos=new sdeSolveInner[commonVariables.core];
		for(int i=0;i<commonVariables.core;i++) {
			proc[i]=0;
			if(mod>0) gijos[i]=new sdeSolveInner(i, tmp+1, this);
			else gijos[i]=new sdeSolveInner(i, tmp, this);
			mod--;
		}
		this.addWindowListener(new WindowListener() {
            public void windowClosed(WindowEvent arg0) {}
            public void windowActivated(WindowEvent arg0) {}
            public void windowClosing(WindowEvent arg0) {
				for(int i=0;i<commonVariables.core;i++) {
					gijos[i].testi=false;
				}
			}
            public void windowDeactivated(WindowEvent arg0) {}
            public void windowDeiconified(WindowEvent arg0) {}
            public void windowIconified(WindowEvent arg0) {}
            public void windowOpened(WindowEvent arg0) {}
        });
	}
	//checks if realization has correct length
	public void checkRealPoints() {
		int exponent2=(int)(Math.log(commonVariables.realPoints)/matlog2);
		double exponent1=Math.log(commonVariables.realPoints)/matlog2;
		if(exponent2!=exponent1) {
			commonVariables.realPoints=(int)Math.pow(2,exponent2+1);
		}
	}
	//repaiting methods
	public void paint(Graphics g) {
		repaint(g);
	}
	public void repaint(Graphics gx) {
		if(commandLine) {		
			super.repaint();
			return ;
		}
		try {
			Graphics g=centr.getGraphics();
			g.setColor(Color.white);
			g.fillRect(0,0,this.getWidth(),this.getHeight());
			g.setColor(Color.blue);
			for(int i=0;i<commonVariables.core;i++) {
				int tmppx=(int)Math.floor((((double)plotisPx)/((double)commonVariables.core))*i);
				int tmppx2=(int)Math.floor((proc[i])*(((double)plotisPx)/((double)commonVariables.core))/100.0);
				g.fillRect(10+tmppx,5,tmppx2,20);
			}
			g.fillRect(10,30,(int)Math.floor(plotisPx*((double)tproc)/(100.0*((double)commonVariables.core))),20);
			g.setColor(Color.black);
			for(int i=0;i<commonVariables.core;i++) {
				int tmppx=(int)Math.floor((((double)plotisPx)/((double)commonVariables.core))*i);
				int tmppx2=(int)Math.floor(((double)plotisPx)/((double)commonVariables.core));
				g.drawRect(10+tmppx,5,tmppx2,20);
			}
			g.drawRect(10,30,plotisPx,20);
			g.setColor(Color.red);
			for(int i=0;i<commonVariables.core;i++) {
				int tmppx=(int)Math.floor((((double)plotisPx)/((double)commonVariables.core))*i);
				int tmppx2=(int)Math.floor(((double)plotisPx)/((double)commonVariables.core));
				g.drawString(proc[i]+"%",10+tmppx+Math.max((tmppx2-strwidth)/2,0),25-Math.max((20-strheight)/2,0));
			}
			g.drawString((tproc/commonVariables.core)+"%",10+Math.max((plotisPx-strwidth)/2,0),50-Math.max((20-strheight)/2,0));
		} catch(Exception e) {
			super.repaint();
		}
	}
	public void update(Graphics g) {
		repaint(g);
	}
	//reporting progress to command line
	public void reportTerm() {
		if(tproc/commonVariables.core>dtproc) {
			dtproc=tproc/commonVariables.core;
			System.out.println((tproc/commonVariables.core)+"% done");
			System.out.flush();
		}
	}
	//function used from outside to put in results
	synchronized public void put(double[] p, double[] s) {
		if((p==null)&&(s==null)) {
			baige++;
			if(baige>=commonVariables.core) {
				if(terminateAfter) System.exit(0);
				this.setVisible(false);
				parrent.put(null,null);
				parrent.menuEnabled(true);
			}
			if(!canceled) {
				test.stop();
				if(!commandLine) {
					setTitle("Canceling");
					repaint();
				} else if(!noOut) {
					System.out.println("Canceling...");
					System.out.flush();
				}
				if((commandLine)&&(!noOut)) {
					System.out.println("Everything done");
					System.out.flush();
				}
				canceled=true;
			}
		}
		if(canceled) return ;
		baige++;
		for(int i=0;i<p.length;i++) pdf[i]+=p[i];
		for(int i=0;i<s.length;i++) spec[i]+=s[i];
		if(baige>=commonVariables.core) {
			test.stop();
			for(int i=0;i<p.length;i++) pdf[i]/=((double)commonVariables.core);
			for(int i=0;i<s.length;i++) spec[i]/=((double)commonVariables.core);
			if(!commandLine) {
				setTitle("Outputing");
				repaint();
			} else if(!noOut) {
				System.out.println("Outputing results...");
				System.out.flush();
			}
			double[][] pdfmas=pdfModification(pdf,commonFunctions.LogBase10(commonVariables.pdfScale),logStep);
			pdf=null;
			double[][] specmas=specModification(spec);
			spec=null;
			String tmp="";
			if((output)||(outputGraph>-1)) {
				//changeing output template
				tmp=commonVariables.outputStr.replaceAll("\\[l\\]",""+commonVariables.lambda);
				tmp=tmp.replaceAll("\\[e\\]",""+commonVariables.epsilon);
				tmp=tmp.replaceAll("\\[n\\]",""+commonVariables.eta);
				tmp=tmp.replaceAll("\\[k\\]",""+commonVariables.kappa);
				tmp=tmp.replaceAll("\\[t\\]",""+commonVariables.tau);
				tmp=tmp.replaceAll("\\[l2\\]",""+commonVariables.lambda2);
				tmp=tmp.replaceAll("\\[rb\\]",""+commonVariables.r0b);
				tmp=tmp.replaceAll("\\[ra\\]",""+commonVariables.r0a);
				tmp=tmp.replaceAll("\\[ex\\]",""+commonVariables.experiment);
				tmp=tmp.replaceAll("\\[xmx\\]",""+commonVariables.xmax);
				tmp=tmp.replaceAll("\\^","");
			}
			if(output) {
				//outputing text
				outputarr(pdfmas,tmp+".dist");
				outputarr(specmas,tmp+".spec");
			}
			if(outputGraph>-1) {
				//outputing graphics
				String drop="";
				if(commonVariables.outputStr.indexOf("[ex]")>-1) {
					drop+=commonVariables.experiment+" ";
				}
				if(commonVariables.outputStr.indexOf("[l]")>-1) {
					drop+=commonVariables.lambda+" ";
				}
				if(commonVariables.outputStr.indexOf("[e]")>-1) {
					drop+=commonVariables.epsilon+" ";
				}
				if(commonVariables.outputStr.indexOf("[n]")>-1) {
					drop+=commonVariables.eta+" ";
				}
				if(commonVariables.outputStr.indexOf("[k]")>-1) {
					drop+=commonVariables.kappa+" ";
				}
				if(commonVariables.outputStr.indexOf("[t]")>-1) {
					drop+=commonVariables.tau+" ";
				}
				if(commonVariables.outputStr.indexOf("[l2]")>-1) {
					drop+=commonVariables.lambda2+" ";
				}
				if(commonVariables.outputStr.indexOf("[rb]")>-1) {
					drop+=commonVariables.r0b+" ";
				}
				if(commonVariables.outputStr.indexOf("[ra]")>-1) {
					drop+=commonVariables.r0a+" ";
				}
				if(commonVariables.outputStr.indexOf("[xmx]")>-1) {
					drop+=commonVariables.xmax+" ";
				}
				plotComponent nonExistent=new plotComponent();
				nonExistent.ini(commonVariables.graphWidth,commonVariables.graphHeight);
				double xmin=Double.MAX_VALUE;
				double xmax=-Double.MAX_VALUE;
				double ymin=Double.MAX_VALUE;
				double ymax=-Double.MAX_VALUE;
				for(int i=0;i<pdfmas.length;i++) {
					xmin=Math.min(xmin,pdfmas[i][0]);
					xmax=Math.max(xmax,pdfmas[i][0]);
					ymin=Math.min(ymin,pdfmas[i][1]);
					ymax=Math.max(ymax,pdfmas[i][1]);
				}
				if(Math.ceil(ymin)-ymin>0.5) ymin=Math.ceil(ymin)-1;
				else ymin=Math.ceil(ymin)-0.5;
				if(Math.floor(ymax)-ymax<-0.5) ymax=Math.floor(ymax)+1;
				else ymax=Math.floor(ymax)+0.5;
				nonExistent.setLimits(xmin,xmax,ymin,ymax);
				double[] tmpa=null;
				double[] tmpa2=null;
				String pap="";
				if(makeApprox) {
					tmpa=commonFunctions.approxim(pdfmas,commonVariables.approx[0],commonVariables.approx[1]);
					if(tmpa!=null) {
						pap=commonFunctions.round(tmpa[0],3)+"*x";
						if(tmpa[1]>=0) pap+="+"+commonFunctions.round(tmpa[1],3);
						else pap+=commonFunctions.round(tmpa[1],3);
						drop+=commonFunctions.round(tmpa[0],3)+" "+commonFunctions.round(tmpa[1],3);
						nonExistent.setPlotLabel("PDF ("+pap+")");
					} else {
						drop+="--- ---";
						nonExistent.setPlotLabel("PDF");
					}
				} else {
					nonExistent.setPlotLabel("PDF");
				}
				nonExistent.useGridlines(true);
				nonExistent.clearPlot(false);
				nonExistent.drawAxis();
				nonExistent.plotArray(pdfmas,Color.darkGray,true);
				double[][] outarr=null;
				double[][] outarr2=null;
				if((makeApprox)&&(tmpa!=null)) {
					outarr=new double[commonVariables.outPoints+1][2];
					double zingsnis=(nonExistent.xMax-nonExistent.xMin)/((double)commonVariables.outPoints);
					for(int i=0;i<outarr.length;i++) {
						outarr[i][0]=nonExistent.xMin+i*zingsnis;
						outarr[i][1]=tmpa[0]*outarr[i][0]+tmpa[1];
					}
					nonExistent.plotArray(outarr,Color.red,true);
				}
				if(outputGraph==0) nonExistent.save(tmp+"[pdf].png");
				else nonExistent.save(tmp+"[pdf].svg");
				xmin=Double.MAX_VALUE;
				xmax=-Double.MAX_VALUE;
				ymin=Double.MAX_VALUE;
				ymax=-Double.MAX_VALUE;
				for(int i=0;i<specmas.length;i++) {
					xmin=Math.min(xmin,specmas[i][0]);
					xmax=Math.max(xmax,specmas[i][0]);
					ymin=Math.min(ymin,specmas[i][1]);
					ymax=Math.max(ymax,specmas[i][1]);
				}
				if(Math.ceil(ymin)-ymin>0.5) ymin=Math.ceil(ymin)-1;
				else ymin=Math.ceil(ymin)-0.5;
				if(Math.floor(ymax)-ymax<-0.5) ymax=Math.floor(ymax)+1;
				else ymax=Math.floor(ymax)+0.5;
				nonExistent.setLimits(xmin,xmax,ymin,ymax);
				tmpa2=null;
				if(commonVariables.simpleSDE) {
					if(makeApprox) {
						tmpa=commonFunctions.approxim(specmas,commonVariables.approx[6],commonVariables.approx[7]);
						if(tmpa!=null) {
							drop+=" "+commonFunctions.round(tmpa[0],3)+" "+commonFunctions.round(tmpa[1],3);
							pap=commonFunctions.round(tmpa[0],3)+"*x";
							if(tmpa[1]>=0) pap+="+"+commonFunctions.round(tmpa[1],3);
							else pap+=commonFunctions.round(tmpa[1],3);
							nonExistent.setPlotLabel("Power spectra density ("+pap+")");
						} else {
							drop+=" --- ---";
							nonExistent.setPlotLabel("Power spectra density");
						}
					} else {
						nonExistent.setPlotLabel("Power spectra density");
					}
				} else {
					if(makeApprox) {
						tmpa=commonFunctions.approxim(specmas,commonVariables.approx[2],commonVariables.approx[3]);
						tmpa2=commonFunctions.approxim(specmas,commonVariables.approx[4],commonVariables.approx[5]);
						pap="";
						if(tmpa!=null) {
							drop+=" "+commonFunctions.round(tmpa[0],3)+" "+commonFunctions.round(tmpa[1],3);
							pap=commonFunctions.round(tmpa[0],3)+"*x";
							if(tmpa[1]>=0) pap+="+"+commonFunctions.round(tmpa[1],3);
							else pap+=commonFunctions.round(tmpa[1],3);
						} else {
							drop+=" --- ---";
						}
						if(tmpa2!=null) {
							drop+=" "+commonFunctions.round(tmpa2[0],3)+" "+commonFunctions.round(tmpa2[1],3);
							if(tmpa!=null) pap+=",";
							pap+=" "+commonFunctions.round(tmpa2[0],3)+"*x";
							if(tmpa2[1]>=0) pap+="+"+commonFunctions.round(tmpa2[1],3);
							else pap+=commonFunctions.round(tmpa2[1],3);
						} else {
							drop+=" --- ---";
						}
						if((tmpa!=null)&&(tmpa2!=null)&&(tmpa2[0]!=tmpa[0])) {
							drop+=" "+commonFunctions.round((tmpa[1]-tmpa2[1])/(tmpa2[0]-tmpa[0]),3);
						} else {
							drop+=" --- ---";
						}						
						if(!pap.equalsIgnoreCase("")) nonExistent.setPlotLabel("Power spectra density ("+pap+")");
						else nonExistent.setPlotLabel("Power spectra density");
					} else {
						nonExistent.setPlotLabel("Power spectra density");
					}
				}
				nonExistent.useGridlines(true);
				nonExistent.clearPlot(false);
				nonExistent.drawAxis();
				nonExistent.plotArray(specmas,Color.darkGray,true);
				outarr=null;
				outarr2=null;
				double zingsnis=0;
				if(makeApprox) {
					if(tmpa!=null) outarr=new double[commonVariables.outPoints+1][2];
					if(tmpa2!=null) outarr2=new double[commonVariables.outPoints+1][2];
					zingsnis=(nonExistent.xMax-nonExistent.xMin)/((double)commonVariables.outPoints);
					for(int i=0;i<outarr.length;i++) {
						if(tmpa!=null) {
							outarr[i][0]=nonExistent.xMin+i*zingsnis;
							outarr[i][1]=tmpa[0]*outarr[i][0]+tmpa[1];
						}
						if(tmpa2!=null) {
							outarr2[i][0]=nonExistent.xMin+i*zingsnis;
							outarr2[i][1]=tmpa2[0]*outarr2[i][0]+tmpa2[1];
						}
					}
					if(outarr!=null) nonExistent.plotArray(outarr,Color.red,true);
					if(outarr2!=null) nonExistent.plotArray(outarr2,Color.red,true);
				}
				if(outputGraph==0) nonExistent.save(tmp+"[spec].png");
				else nonExistent.save(tmp+"[spec].svg");
				if((approxDrop)&&(makeApprox)) {
					try {
						BufferedWriter out=new BufferedWriter(new FileWriter(commonVariables.outputStr.replaceAll("\\^","")+".approxdrop",true));
						out.write(drop+commonVariables.lineBreak);
						out.close();
					} catch(Exception e) {
						e.printStackTrace();
					}
				}
			}
			if((commandLine)&&(!noOut)) {
				System.out.println("Everything done");
				System.out.flush();
			}
			if(terminateAfter) System.exit(0);
			this.setVisible(false);
			parrent.put(pdfmas,specmas);
			parrent.menuEnabled(true);
			//dispose() for unknown reasons doesn't work
			//in main function using windowHandler=null to dispose of window
		}
	}
	//outside programs report their progress here
	synchronized public void report(int i, int kiek) {
		int tmpproc=tproc-proc[i];
		proc[i]=kiek;
		tmpproc+=proc[i];
		tproc=tmpproc;
	}
	//linear spectra to logarithmic spectra
	public double[][] specModification(double[] spec) {
		double normavimas=commonFunctions.LogBase10(2*60*(commonVariables.tau/commonVariables.tau60)/spec.length);
		double skale=commonFunctions.LogBase10(spec.length)+commonFunctions.LogBase10(60*(commonVariables.tau/commonVariables.tau60));
		double llim=0;
		double rlim=commonFunctions.LogBase10(spec.length/2.0);
		double lstep=(rlim-llim)/((double)commonVariables.outPoints);
		double clim=llim+lstep;
		int i=1;
		int intervale=0;
		double[][] rez=new double[commonVariables.outPoints+1][2];
		int panaudota=0;
		double total=0;
		double oldX=0;
		while(clim<=rlim) {
			while(commonFunctions.LogBase10(i)<clim) {
				total+=spec[i];
				i++;
				intervale++;
			}
			if(total>0) {
				if(panaudota==0) {
					oldX=Math.pow(10,clim-skale);
					rez[panaudota][0]=commonFunctions.LogBase10(oldX/2.0);
					rez[panaudota][1]=commonFunctions.LogBase10(total/((double)intervale));//oldX);
				} else {
					double newX=Math.pow(10,clim-skale);
					rez[panaudota][0]=commonFunctions.LogBase10((newX+oldX)/2.0);
					rez[panaudota][1]=commonFunctions.LogBase10(total/((double)intervale));//(newX-oldX));
					oldX=newX;
				}
				/*if(panaudota==0) {
					oldX=Math.pow(10,clim);
					rez[panaudota][0]=commonFunctions.LogBase10((oldX/Math.pow(10,skale))/2.0);
					rez[panaudota][1]=commonFunctions.LogBase10(total/oldX);//((double)intervale));
				} else {
					double newX=Math.pow(10,clim);
					rez[panaudota][0]=commonFunctions.LogBase10((newX+oldX)/(2.0*Math.pow(10,skale)));
					rez[panaudota][1]=commonFunctions.LogBase10(total/(newX-oldX));//((double)intervale));
					oldX=newX;
				}*/
				rez[panaudota][1]+=normavimas;
				panaudota++;
			}
			intervale=0;
			total=0;
			clim+=lstep;
		}
		double[][] rez2=new double[panaudota][2];
		for(int ii=0;ii<panaudota;ii++) {
			rez2[ii][0]=rez[ii][0];
			rez2[ii][1]=rez[ii][1];
		}
		/*movavg time window 3*/
		for(int ii=0;ii<panaudota;ii++) {
			if((ii>0)&&(ii<panaudota-1)) {
				rez2[ii][1]=(rez2[ii-1][1]+rez2[ii][1]+rez2[ii+1][1])/3.0;
			}
		}
		return rez2;
	}
	//linear pdf to logarithmic pdf
	public double[][] pdfModification(double[] pdf, double llim, double lstep) {
		double[][] rez=new double[pdf.length][2];
		double rlim=llim+lstep*(pdf.length+1);
		int panaudota=0;
		for(int i=0;i<pdf.length;i++) {
			if(pdf[i]>0) {
				rez[panaudota][0]=llim+i*lstep;
				if(panaudota==0) rez[panaudota][1]=commonFunctions.LogBase10(pdf[i]/(Math.pow(10,rez[panaudota][0])-Math.pow(10,rez[panaudota][0]-lstep)));
				else rez[panaudota][1]=commonFunctions.LogBase10(pdf[i]/(Math.pow(10,rez[panaudota][0])-Math.pow(10,rez[panaudota-1][0])));
				panaudota++;
			}
		}
		double[][] rez2=new double[panaudota][2];
		for(int ii=0;ii<panaudota;ii++) {
			rez2[ii][0]=rez[ii][0];
			rez2[ii][1]=rez[ii][1];
		}		
		return rez2;
	}
	//output to text file
	public void outputarr(double[][] arr, String name) {
		BufferedWriter out=null;
		String fileName=name;
		try {
			File file = new File(fileName);
			file.createNewFile();
			out = new BufferedWriter(new FileWriter(fileName));
			for(int i=0;i<arr.length;i++) {
				out.write(commonFunctions.round(arr[i][0],6)+" "+commonFunctions.round(arr[i][1],6)+""+commonVariables.lineBreak+"");
			} 
			out.close();
		} catch (Exception e) {
			System.out.println("#002a Error while writing file "+fileName);
			e.printStackTrace();
		}
	}
}

//main sde solution class formalized as thread
class sdeSolveInner implements Runnable {
	public boolean testi=true;
	//is commonFunctions
	public double matlog2=Math.log(2);
	public double matlog10=Math.log(10);
	//------------------
	//is commonVariables
	private double xmax;
	private int realPoints;
	private double lambda;
	private double epsilon;
	private double eta;
	private double kappa;
	private double tau;
	private double deltaT;
	private boolean useLimits;
	private boolean oldDiffusionLimiter;
	private boolean simpleSDE;
	private double lambda2;
	private double r0b;
	private double r0a;
	private boolean noise;
	private double pdfMax;
	private double pdfScale;
	private int outPoints;
	private boolean minusMean;
	//------------------
	private sdeSolveOuter parrent=null;
	private Random generator=new Random();
	private int nr=0;
	private int realizationsInner=10;
	private int realMade=0;
	private double[] xmas;
	private double[] pdf;
	private double[] spec;
	private double[] pdft;
	private double[] spect;
	private double[] iter;
	private double[] iterl;
	private double nextTimeLimit=0;
	private double integral=0;
	private double logStep;
	private int done=0;
	private double reportNext=0;
	private int proc=0;
	//empty constructor
	public sdeSolveInner() {}
	//actual constructor
	public sdeSolveInner(int nrThread, int r, sdeSolveOuter par) {
		xmax=commonVariables.xmax;
		realPoints=commonVariables.realPoints;
		lambda=commonVariables.lambda;
		epsilon=commonVariables.epsilon;
		eta=commonVariables.eta;
		kappa=commonVariables.kappa;
		tau=commonVariables.tau;
		deltaT=commonVariables.deltaT;
		useLimits=commonVariables.useLimits;
		oldDiffusionLimiter=commonVariables.oldDiffusionLimiter;
		simpleSDE=commonVariables.simpleSDE;
		lambda2=commonVariables.lambda2;
		r0b=commonVariables.r0b;
		r0a=commonVariables.r0a;
		noise=commonVariables.noise;
		pdfMax=commonVariables.pdfMax;
		pdfScale=commonVariables.pdfScale;
		outPoints=commonVariables.outPoints;
		minusMean=commonVariables.minusMean;
		//function parameters explained:
		nr=nrThread;
		realizationsInner=r;
		parrent=par;
		//-----
		reportNext=realizationsInner*((double)realPoints/100.0);
		proc=1;
		xmas=null;
		logStep=(LogBase10((double)pdfMax)-LogBase10(pdfScale))/((double)outPoints);
		pdf=new double[outPoints+1];
		for(int i=0;i<pdf.length;i++) pdf[i]=0;
		spec=new double[realPoints];
		for(int i=0;i<spec.length;i++) spec[i]=0;
		new Thread(this, "Number of thread "+nr).start();
	}
	//thread run method
	public void run() {
		for(realMade=0;realMade<realizationsInner;realMade++) {
			realization();
			if(!testi) {
				parrent.put(null,null);
				return ;
			}
		}
		for(int i=0;i<pdf.length;i++) pdf[i]/=((double)realizationsInner);
		for(int i=0;i<=spec.length/2;i++) spec[i]/=((double)realizationsInner);
		parrent.put(pdf,spec);
	}
	//calculates single realization
	private void realization() {
		pdft=new double[pdf.length];
		for(int i=0;i<pdft.length;i++) pdft[i]=0;
		xmas=new double[realPoints];
		done=0;//next xmas index to be written
		integral=0;
		nextTimeLimit=tau;//next right integral boundary
		iter=new double[2];//actual iteration data
		iter[0]=0;//time
		if(true) {
			double tmpq=1.0+2.0/lambda;
			double tmpq2=1.0+2.0/lambda2;
			iter[1]=qGaussian(tmpq2,(r0b+r0a*qGaussian(tmpq,Math.sqrt((tmpq-1)/(3-tmpq)),true))*Math.sqrt((tmpq2-1)/(3-tmpq2)),true);//return
		}
		iterl=new double[2];//last iteration data
		iterl[0]=0;//time
		iterl[1]=0;//return
		while((done<xmas.length)&&(testi)) {
			iteration();
			while(reportNext<(done+realMade*realPoints)) {
				proc++;
				reportNext=proc*realizationsInner*((double)realPoints/100.0);
			}
			parrent.report(nr,proc);
		}
		if(!testi) return ;
		for(int i=0;i<pdft.length;i++) pdft[i]/=((double)done);
		for(int i=0;i<pdf.length;i++) pdf[i]+=pdft[i];
		if(minusMean) {
			double tmpMean=0;
			double tmpTmpMean=0;
			for(int i=0;i<realPoints;i++) {
				tmpTmpMean+=xmas[i];
				if(tmpTmpMean>1000) {
					tmpTmpMean/=((double)realPoints);
					tmpMean+=tmpTmpMean;
					tmpTmpMean=0;
				}
			}
			if(tmpTmpMean>0) {
				tmpTmpMean/=((double)realPoints);
				tmpMean+=tmpTmpMean;
				tmpTmpMean=0;
			}
			for(int i=0;i<realPoints;i++) xmas[i]-=tmpMean;
		}
		double[] spect=preformRealFFT(xmas);
		for(int i=0;i<=spec.length/2;i++) spec[i]+=spect[i];
		xmas=null;
		pdft=null;
		spect=null;
	}
	//calculates single iteration
	private void iteration() {
		iterl[0]=iter[0];//time
		iterl[1]=iter[1];//return
		double toRiba=nextTimeLimit-iterl[0];
		double h=h(iterl[1]);
		if(h>deltaT) {
			if(deltaT<toRiba) {
				iter[0]=iterl[0]+deltaT;
				iter[1]=solveDif(iterl[1],deltaT);
				integral+=(iter[1]*deltaT);
			} else {
				if(toRiba>0) {
					iter[0]=iterl[0]+toRiba;
					iter[1]=solveDif(iterl[1],toRiba);
					integral+=(iter[1]*toRiba);
				}
				if(noise) xmas[done]=addOnNoise(Math.abs(integral/tau));
				else xmas[done]=Math.abs(integral/tau);
				done++;
				pdfAdd(pdft,xmas[done-1],pdfScale,logStep);
				integral=0;
				nextTimeLimit+=tau;
			}
		} else {
			if(h<=toRiba) {
				iter[0]=iterl[0]+h;
				iter[1]=solveDif(iterl[1]);
				integral+=(iter[1]*h);
			} else {
				if(toRiba>0) {
					iter[0]=iterl[0]+toRiba;
					iter[1]=solveDif(iterl[1],toRiba);
					integral+=(iter[1]*toRiba);
				}
				if(noise) xmas[done]=addOnNoise(Math.abs(integral/tau));
				else xmas[done]=Math.abs(integral/tau);
				done++;
				pdfAdd(pdft,xmas[done-1],pdfScale,logStep);
				integral=0;
				nextTimeLimit+=tau;
			}
		}
	}
	//append pdf
	private void pdfAdd(double[] pdft, double verte, double pdfs, double logs) {
		int tmp=(int)Math.floor(LogBase10((verte/pdfs))/logs);
		if((tmp>=0)&&(tmp<pdft.length)) {
			pdft[tmp]++;
		}
	}
	//numberical sde sollution
	private double solveDif(double x, double dt) {//using constant step size
		double rez=0;
		rez=x+f(x,dt)+g(x,dt)*generator.nextGaussian();
		if(useLimits) rez=Math.min(Math.max(rez,-xmax),xmax);
		return rez;
	}
	private double solveDif(double x) {//using variable step size
		double rez=x+f(x)+g(x)*generator.nextGaussian();
		if(useLimits) rez=Math.min(Math.max(rez,-xmax),xmax);
		return rez;
	}
	//sde functions
	private double g(double x) {
		return kappa*Math.sqrt(1+x*x);
	}
	private double gc(double x) {
		if(simpleSDE) return Math.pow(1+x*x,eta/2.0);
		double x2=1+x*x;
		double a4=Math.pow(x2,eta/2.0);
		double a3=Math.sqrt(x2)*epsilon+1;
		return a4/a3;
	}
	private double g(double x, double dt) {
		return gc(x)*Math.sqrt(dt);
	}
	private double f(double x) {
		if(simpleSDE) return kappa*kappa*x*(eta-lambda/2.0);
		double a1=0;
		if(oldDiffusionLimiter)	a1=x*x*Math.pow(epsilon,2.0*eta);
		else a1=x*x/(xmax*xmax);
		return kappa*kappa*x*(eta-lambda/2.0-a1);
	}
	private double fc(double x) {
		if(simpleSDE) return (eta-lambda/2.0)*Math.pow(1+x*x,eta-1)*x;
		double a1=0;
		if(oldDiffusionLimiter)	a1=x*x*Math.pow(epsilon,2.0*eta);
		else a1=x*x/(xmax*xmax);
		double x2=1+x*x;
		double a2=Math.pow(x2,eta-1);
		double a3=epsilon*Math.sqrt(x2)+1;
		a3*=a3;
		return (eta-lambda/2.0-a1)*(a2/a3)*x;
	}
	private double f(double x, double dt) {
		return fc(x)*dt;
	}
	private double h(double x) {
		double x2=1+x*x;
		if(simpleSDE) return kappa*kappa*Math.pow(x2,1-eta);
		double a3=Math.sqrt(x2)*epsilon+1;
		a3*=a3;
		double a2=Math.pow(x2,eta-1);
		return kappa*kappa*a3/a2;
	}
	//q gaussian noise
	private double r0(double mar) {
		return r0b+r0a*Math.abs(mar);
	}
	private double addOnNoise(double i) {
		double q=1.0+2.0/lambda2;
		return qGaussian(q,r0(i)*Math.sqrt((q-1)/(3-q)),true);
	}
	public double qLog(double x, double qGen) {
		return (Math.pow(x,1-qGen)-1)/(1-qGen);
	}
	public double qGaussian(double q, boolean abs) {
		double qGen=(1+q)/(3-q);
		double u1=generator.nextDouble();
		double u2=generator.nextDouble();
		if(abs) return Math.abs(Math.sqrt(-2*qLog(u1,qGen))*Math.sin(2*Math.PI*u2));
		return Math.sqrt(-2*qLog(u1,qGen))*Math.sin(2*Math.PI*u2);
	}
	public double qGaussian(double q, double sq, boolean abs) {
		return sq*qGaussian(q,abs);
	}
	//Faster lg calculation
	public double LogBase10(double a) {
		return Math.log(a)/matlog10;
	}
	//Fast fourier transform function
	//using algorithm detailed in dspguide.com chapter 12
	public double[] preformRealFFT(double[] arrx) {
		double[] rex=new double[arrx.length];
		System.arraycopy(arrx,0,rex,0,arrx.length);
		int n=rex.length;
		double[] imx=new double[n];
		for(int j=0;j<n;j++) {
			imx[j]=0;
		}
		int nm1=n-1;
		int nd2=(int)(n/2);
		int m=cint(Math.log(n)/matlog2);
		int j=nd2;
		for(int i=1;i<nm1;i++) {//bit reversal
			if(i<=j) {
				double tr=rex[j];
				double ti=imx[j];
				rex[j]=rex[i];
				imx[j]=imx[i];
				rex[i]=tr;
				imx[i]=ti;
			}
			int k=nd2;
			while(k<=j) {
				j-=k;
				k=(int)(k/2);
			}
			j+=k;
		}		
		for(int l=1;l<m+1;l++) {
			int le=cint(Math.pow(2,l));
			int le2=(int)(le/2);
			double ur=1;
			double ui=0;
			double tr=0;
			double ti=0;
			double sr=Math.cos(Math.PI/((double)le2));
			double si=-Math.sin(Math.PI/((double)le2));
			for(j=1;j<le2+1;j++) {
				int jm1=j-1;
				for(int i=jm1;i<=nm1;i+=le) {
					int ip=i+le2;
					tr=rex[ip]*ur-imx[ip]*ui;
					ti=imx[ip]*ur+rex[ip]*ui;
					rex[ip]=rex[i]-tr;
					imx[ip]=imx[i]-ti;
					rex[i]+=tr;
					imx[i]+=ti;
				}
				tr=ur;
				ur=tr*sr-ui*si;
				ui=tr*si+ui*sr;
			}
		}
		for(int i=0;i<=n/2;i++) {
			rex[i]=(rex[i]*rex[i]+imx[i]*imx[i]);
		}
		return rex;
	}
	//special approximation function used by FFT function
	private int cint(double expr) {
		double dif=Math.abs((expr-(int)expr));
		if(dif==0.5) {
			if(((int)expr)%2==0) {
				return (int)expr;
			} else {
				return (int)expr+1;
			}
		} else if(dif>0.5) {
			return (int)expr+1;
		} else {
			return (int)expr;
		}
	}
}

//common function class
class commonFunctions {
	public static double matlog10=Math.log(10);
	//approximation function
	//using least squares method
	static public double[] approxim(double[][] arry, double xmin, double xmax) {
		double[] rez=new double[2];
		double xtot=0;
		double ytot=0;
		double x2tot=0;
		double xytot=0;
		double y2tot=0;
		int N=0;
		for(int i=0;i<arry.length;i++) {
			try {
				double tx=arry[i][0];
				double ty=arry[i][1];
				if((tx>=xmin)&&(tx<=xmax)) {
					xtot+=tx;
					ytot+=ty;
					x2tot+=(tx*tx);
					xytot+=(tx*ty);
					y2tot+=(ty*ty);
					N++;
				}
			} catch(Exception e) {
				System.out.println("#005a Error while approximating.");
			}
		}
		double aproks_delta=((double)N)*x2tot-xtot*xtot;
		double a=(((double)1)/((double)aproks_delta))*(N*xytot-xtot*ytot);
		double b=(((double)1)/((double)aproks_delta))*(x2tot*ytot-xtot*xytot);
		rez[0]=a;
		rez[1]=b;
		return rez;
	}
	//Faster lg calculation
	public static double LogBase10(double a) {
		return Math.log(a)/matlog10;
	}
	//advanced rounding function
	public static double round(double d, int i) {
		int j = i;
		BigDecimal bigdecimal = new BigDecimal(d);
		bigdecimal = bigdecimal.setScale(j,bigdecimal.ROUND_HALF_UP);
		d = bigdecimal.doubleValue();
		return d;
	}
}

//static class holding global variables
class commonVariables {
	public static long experiment=System.currentTimeMillis();
	public static boolean experimentTime=true;
	public static int core=1;	
	//model parameters
	public static double xmax=1000;
	public static int realPoints=262144;
	public static int realizations=10;
	public static double lambda=3.6;
	public static double epsilon=0.017;//0.01
	public static double eta=2.5;
	public static double kappa=0.0316;//0.1
	public static double tau=0.0002;//0.0001
	public static double tau60=0.0002;//0.0001
	public static double deltaT=tau;
	public static boolean useLimits=false;
	public static boolean oldDiffusionLimiter=false;
	public static boolean simpleSDE=false;
	public static double lambda2=5.0;//4.0
	public static double r0b=1.0;//0.9
	public static double r0a=0.4;//1.5*0.2
	public static boolean noise=true;
	//mixed
	public static double pdfMax=1000.0;
	public static double pdfScale=0.001;
	public static int outPoints=640;
	public static String outputStr="rez/ret";
	public static int graphHeight=480;
	public static int graphWidth=640;	
	public static boolean windowCentered=true;
	public static String lineBreak=System.getProperty("line.separator");
	public static double[] approx=new double[8];
	public static boolean minusMean=true;
}

//plotting component
class plotComponent extends javax.swing.JComponent {
	//graphical buffer
	private BufferedImage Buffer;
	private Graphics Buffer2;
	//graph limits
	public double xMin=-1;
	public double xMax=1;
	private double yMin=-1;
	private double yMax=1;
	private double xDif=2;
	private double yDif=2;
	//graph precission
	private int precissionLabelX=-2;
	private int precissionLabelY=-2;
	//graph ticks
	private double[] ticksXCoord=null;
	private double[] ticksYCoord=null;
	private double[] smallTicksXCoord=null;
	private double[] smallTicksYCoord=null;
	private String[] ticksXLabel=null;
	private String[] ticksYLabel=null;
	private boolean useSmallTicksX=true;
	private boolean useSmallTicksY=true;
	//graph margins
	private int leftMargin=10;
	private int topMargin=10;
	private int rightMargin=10;
	private int bottomMargin=10;
	private int enhancedLeftMargin=11;
	private int enhancedTopMargin=11;
	private int enhancedBottomMargin=11;
	private int graphWidth=0;
	private int graphHeight=0;
	//graph label
	private boolean usePlotLabel=false;
	private int plotLabelWidth=0;
	private String labelToPlot="";
	//graph inner size
	private int distanceXInPx=-1;
	private int distanceYInPx=-1;
	//font variables
	private String fontName="Verdana";
	private int fontSize=12;
	private FontMetrics fm;
	//gridlines
	private boolean plotGridlines=false;
	//color scheme
	public Color backgroundColor=Color.white;
	public Color backgroundColor2=Color.white;
	private Color axisColor=Color.black;
	private Color gridlineColor=Color.lightGray;
	private Color activeDrawingColor=Color.white;
	//plotLineNext algorithm variables
	private int lastPointX=Integer.MIN_VALUE;
	private int lastPointY=Integer.MIN_VALUE;
	private int plotLine_x=Integer.MIN_VALUE;
	private int plotLine_max=Integer.MIN_VALUE;
	private int plotLine_min=Integer.MAX_VALUE;
	private int plotLine_which=0;
	//svg variables
	public boolean useSvg=true;
	private String svgTemplatePolylines="";
	private String svgCurrentPolyline="";
	private int renderSizeX=100;
	private int renderSizeY=100;
	//Basic functions
	//	constructor (0 level)
	public plotComponent() {}
	//	bound setting (1 level)
	public void setBounds(int x, int y, int w, int h) {
		super.setBounds(x,y,w,h);
	}
	//	repaint functions
	public void paint(Graphics g) {
		if(Buffer!=null) {
			g.setColor(backgroundColor);
			g.fillRect(0,0,getWidth(),getHeight());
			double aspect=((double)graphWidth)/((double)graphHeight);
			double aspect2=((double)getWidth())/((double)getHeight());
			if(aspect>aspect2) {
				g.drawImage(Buffer,0,0,getWidth(),(int)(getWidth()/aspect),this);
			} else if(aspect<aspect2) {
				g.drawImage(Buffer,0,0,(int)(aspect*getHeight()),getHeight(),this);
			} else {
				g.drawImage(Buffer,0,0,getWidth(),getHeight(),this);
			}
		}
	}
	public void update(Graphics g) {
		if(Buffer!=null) {
			g.setColor(backgroundColor);
			g.fillRect(0,0,getWidth(),getHeight());
			double aspect=((double)graphWidth)/((double)graphHeight);
			double aspect2=((double)getWidth())/((double)getHeight());
			if(aspect>aspect2) {
				g.drawImage(Buffer,0,0,getWidth(),(int)(getWidth()/aspect),this);
			} else if(aspect<aspect2) {
				g.drawImage(Buffer,0,0,(int)(aspect*getHeight()),getHeight(),this);
			} else {
				g.drawImage(Buffer,0,0,getWidth(),getHeight(),this);
			}
		}
	}
	public void repaint(Graphics g) {
		if(Buffer!=null) {
			g.setColor(backgroundColor);
			g.fillRect(0,0,getWidth(),getHeight());
			double aspect=((double)graphWidth)/((double)graphHeight);
			double aspect2=((double)getWidth())/((double)getHeight());
			if(aspect>aspect2) {
				g.drawImage(Buffer,0,0,getWidth(),(int)(getWidth()/aspect),this);
			} else if(aspect<aspect2) {
				g.drawImage(Buffer,0,0,(int)(aspect*getHeight()),getHeight(),this);
			} else {
				g.drawImage(Buffer,0,0,getWidth(),getHeight(),this);
			}
		}
	}
	//Initialization (2 level)
	public void ini(String fV, int fD, int wi, int he) {
		Buffer=new BufferedImage(wi,he,BufferedImage.TYPE_INT_RGB);
		graphWidth=wi;
		graphHeight=he;
		Buffer2=Buffer.getGraphics();
		Buffer2.setFont(new Font(fV,Font.PLAIN,fD));
		fm=Buffer2.getFontMetrics ();
		Font tmpf=Buffer2.getFont();
		fontName=tmpf.getFontName();
		fontSize=tmpf.getSize();
		setSVGOptions(wi,he);
	}
	public void ini(int wi, int he) {
		ini(fontName,fontSize,wi,he);
	}
	public void ini() {
		ini(fontName,fontSize,640,480);
	}
	//basic graph setting (3 level)
	public void setLimits(double xmin, double xmax, double ymin, double ymax) {
		xMin=xmin;
		xMax=xmax;
		xDif=xMax-xMin;
		yMin=ymin;
		yMax=ymax;
		yDif=yMax-yMin;
	}
	public void setPlotLabel(String lab) {
		labelToPlot=lab;
		usePlotLabel=true;
		plotLabelWidth=fm.stringWidth(lab);
	}
	public void disablePlotLabel() {
		usePlotLabel=false;
	}
	public void useGridlines(boolean u) {
		plotGridlines=u;
	}
	public void clearPlot() {
		clearPlot(true);
	}
	public void clearPlot(boolean clearG) {
		Buffer2.setColor(backgroundColor);
		Buffer2.fillRect(0,0,graphWidth,graphHeight);
		if(clearG) {
			Graphics g=getGraphics();
			if(g!=null) g.drawImage(Buffer,0,0,getWidth(),getHeight(), this);
		}
		svgTemplatePolylines="";
	}
	//axis drawing (4 level)
	public void drawAxis() {
		calculatePrecission();
		calculateTicks();
		calculateMargins();
		calculateSmallTicks();
		fillFrame();
		drawFrame();
		drawTicks();
		drawSmallTicks();
		if(plotGridlines) drawGridlines();
		if(usePlotLabel) plotLabel();
	}
	private void drawGridlines() {
		Buffer2.setColor(gridlineColor);
		int apatineXAsis=graphHeight-bottomMargin;
		int desineYAsis=graphWidth-rightMargin;
		int xPos=0;
		int tmp=leftMargin+1;
		for(int i=0;i<ticksXCoord.length;i++) {
			xPos=tmp+(int)Math.floor(ticksXCoord[i]*distanceXInPx);
			Buffer2.drawLine(xPos,apatineXAsis,xPos,topMargin);
		}
		tmp=apatineXAsis-1;
		for(int i=0;i<ticksYCoord.length;i++) {
			xPos=tmp-(int)Math.floor(ticksYCoord[i]*distanceYInPx);
			Buffer2.drawLine(leftMargin,xPos,desineYAsis,xPos);
		}
	}
	private void plotLabel() {
		Buffer2.setColor(axisColor);
		Buffer2.drawString(labelToPlot,leftMargin+1+(distanceXInPx-plotLabelWidth)/2,topMargin-fm.getDescent()-10);
	}
	private void calculateTicks() {
		double incX=smallestScaleIncrement(precissionLabelX);
		double startTick=roundToMultiplier(xMin,incX);
		int amountTicksX=0;
		while((amountTicksX==0)||(amountTicksX>6)||(amountTicksX<3)) {
			startTick=roundToMultiplier(xMin,incX);
			while(startTick<xMin) startTick+=incX;
			amountTicksX=0;
			double xMinT=startTick;
			for(;xMinT<=xMax;xMinT+=incX) {
				amountTicksX++;
			}
			if(amountTicksX<3) incX/=2;
			else if(amountTicksX>6) incX*=2;
		}
		ticksXCoord=new double[amountTicksX];
		ticksXLabel=new String[amountTicksX];
		while(startTick<xMin) startTick+=incX;
		int i=0;
		int decimalPlace=1;
		if(precissionLabelX-2<0) decimalPlace=2-precissionLabelX;
		while((startTick<=xMax)&&(i<amountTicksX)) {
			ticksXCoord[i]=round((startTick-xMin)/xDif,3);
			ticksXLabel[i]=round(startTick,decimalPlace)+"";
			startTick+=incX;
			i++;
		}
		if(i<amountTicksX) {
			double[] tmp=new double[i];
			String[] tmps=new String[i];
			System.arraycopy(ticksXCoord,0,tmp,0,i);
			System.arraycopy(ticksXLabel,0,tmps,0,i);
			ticksXCoord=new double[i];
			ticksXLabel=new String[i];
			System.arraycopy(tmp,0,ticksXCoord,0,i);
			System.arraycopy(tmps,0,ticksXLabel,0,i);
		}
		double incY=smallestScaleIncrement(precissionLabelY);
		startTick=roundToMultiplier(yMin,incY);
		int amountTicksY=0;
		while((amountTicksY==0)||(amountTicksY>6)||(amountTicksY<3)) {
			startTick=roundToMultiplier(yMin,incY);
			while(startTick<yMin) startTick+=incY;
			amountTicksY=0;
			double xMinT=startTick;
			for(;xMinT<=yMax;xMinT+=incY) {
				amountTicksY++;
			}
			if(amountTicksY<3) incY/=2;
			else if(amountTicksY>6) incY*=2;
		}
		ticksYCoord=new double[amountTicksY];
		ticksYLabel=new String[amountTicksY];
		while(startTick<yMin) startTick+=incY;
		i=0;
		decimalPlace=1;
		if(precissionLabelY-2<0) decimalPlace=2-precissionLabelY;
		while((startTick<=yMax)&&(i<amountTicksY)) {
			ticksYCoord[i]=round((startTick-yMin)/yDif,3);
			ticksYLabel[i]=round(startTick,decimalPlace)+"";
			startTick+=incY;
			i++;
		}
		if(i<amountTicksY) {
			double[] tmp=new double[i];
			String[] tmps=new String[i];
			System.arraycopy(ticksYCoord,0,tmp,0,i);
			System.arraycopy(ticksYLabel,0,tmps,0,i);
			ticksYCoord=new double[i];
			ticksYLabel=new String[i];
			System.arraycopy(tmp,0,ticksYCoord,0,i);
			System.arraycopy(tmps,0,ticksYLabel,0,i);
		}
	}
	private void calculateSmallTicks() {
		//sutvarkyti skaiciavimus
		int amountTicksX=(ticksXCoord.length+1)*9+1;
		smallTicksXCoord=new double[amountTicksX];
		double incX=xDif*(ticksXCoord[1]-ticksXCoord[0])/10.0;
		double startTick=roundToMultiplier(xMin,incX);
		while(startTick<xMin) startTick+=incX;
		int i=0;
		int j=0;
		if(useSmallTicksX) {
			while((startTick<=xMax)&&(i<amountTicksX)) {
				smallTicksXCoord[i]=round((startTick-xMin)/xDif,3);
				int tmpI=i;
				startTick+=incX;
				if(j==ticksXCoord.length) j--;
				if(Math.abs(smallTicksXCoord[i]-ticksXCoord[j])>0.5/(ticksXCoord.length*10+10)) i++;
				if(i>=amountTicksX) break;
				if(smallTicksXCoord[tmpI]>=ticksXCoord[j]) j++;
			}
			while(i<amountTicksX) {
				smallTicksXCoord[i]=-1;
				i++;
			}
		}
		int amountTicksY=(ticksYCoord.length+1)*9+1;
		smallTicksYCoord=new double[amountTicksY];
		double incY=yDif*(ticksYCoord[1]-ticksYCoord[0])/10.0;
		startTick=roundToMultiplier(yMin,incY);
		while(startTick<yMin) startTick+=incY;
		i=0;
		j=0;
		if(useSmallTicksY) {
			while((startTick<=yMax)&&(i<amountTicksY)) {
				smallTicksYCoord[i]=round((startTick-yMin)/yDif,3);
				int tmpI=i;
				startTick+=incY;
				if(j==ticksYCoord.length) j--;
				if(Math.abs(smallTicksYCoord[i]-ticksYCoord[j])>0.5/(ticksYCoord.length*10+10)) i++;
				if(i>=amountTicksY) break;
				if(smallTicksYCoord[tmpI]>=ticksYCoord[j]) j++;
			}
			while(i<amountTicksY) {
				smallTicksYCoord[i]=-1;
				i++;
			}
		}
	}
	private void drawTicks() {
		Buffer2.setColor(axisColor);
		int apatineXAsis=graphHeight-bottomMargin;
		int desineYAsis=graphWidth-rightMargin;
		int xPos=0;
		int tmp=leftMargin+1;
		for(int i=0;i<ticksXCoord.length;i++) {
			xPos=tmp+(int)Math.floor(ticksXCoord[i]*distanceXInPx);
			Buffer2.drawLine(xPos,apatineXAsis,xPos,apatineXAsis+5);
			Buffer2.drawString(ticksXLabel[i],xPos-fm.stringWidth(ticksXLabel[i])/2,apatineXAsis+10+fm.getAscent());
			Buffer2.drawLine(xPos,topMargin,xPos,topMargin-5);
		}
		tmp=apatineXAsis-1;
		for(int i=0;i<ticksYCoord.length;i++) {
			xPos=tmp-(int)Math.floor(ticksYCoord[i]*distanceYInPx);
			Buffer2.drawLine(leftMargin,xPos,leftMargin-5,xPos);
			Buffer2.drawString(ticksYLabel[i],leftMargin-10-fm.stringWidth(ticksYLabel[i]),xPos+fm.getAscent()/2);
			Buffer2.drawLine(desineYAsis,xPos,desineYAsis+5,xPos);
		}
	}	
	private void drawSmallTicks() {
		Buffer2.setColor(axisColor);
		int apatineXAsis=graphHeight-bottomMargin;
		int desineYAsis=graphWidth-rightMargin;
		int xPos=0;
		int tmp=leftMargin+1;
		if(useSmallTicksX) {
			for(int i=0;i<smallTicksXCoord.length;i++) {
				if(smallTicksXCoord[i]<0) break;
				xPos=tmp+(int)Math.floor(smallTicksXCoord[i]*distanceXInPx);
				Buffer2.drawLine(xPos,apatineXAsis+3,xPos,apatineXAsis+5);
				Buffer2.drawLine(xPos,topMargin-3,xPos,topMargin-5);
			}
		}
		tmp=apatineXAsis-1;
		if(useSmallTicksY) {
			for(int i=0;i<smallTicksYCoord.length;i++) {
				if(smallTicksYCoord[i]<0) break;
				xPos=tmp-(int)Math.floor(smallTicksYCoord[i]*distanceYInPx);
				Buffer2.drawLine(leftMargin-3,xPos,leftMargin-5,xPos);
				Buffer2.drawLine(desineYAsis+3,xPos,desineYAsis+5,xPos);
			}
		}
	}
	private void calculatePrecission() {
		precissionLabelX=getDigitScale(xDif,true);
		precissionLabelY=getDigitScale(yDif,false);
	}
	private void calculateMargins() {
		int longestLabelX=0;
		int longestLabelY=0;
		for(int i=0;i<ticksXLabel.length;i++) {
			longestLabelX=Math.max(longestLabelX,fm.stringWidth(ticksXLabel[i]));
		}
		longestLabelX=Math.max(longestLabelX,fm.stringWidth(ticksYLabel[0])/2+1);
		for(int i=0;i<ticksYLabel.length;i++) {
			longestLabelY=Math.max(longestLabelY,fm.stringWidth(ticksYLabel[i]));
		}
		leftMargin=15+Math.max(longestLabelY,longestLabelX/2+1);
		bottomMargin=15+fm.getAscent();
		topMargin=10;
		if(usePlotLabel) topMargin+=(fm.getHeight()+5);
		rightMargin=Math.max(10,longestLabelX/2+1);
		distanceXInPx=graphWidth-leftMargin-rightMargin-2;
		distanceYInPx=graphHeight-topMargin-bottomMargin-2;
		enhancedLeftMargin=leftMargin+1;
		enhancedTopMargin=topMargin+1;
		enhancedBottomMargin=bottomMargin+1;
	}
	private void fillFrame() {
		Buffer2.setColor(backgroundColor2);
		Buffer2.fillRect(leftMargin-5,topMargin-5,(graphWidth-rightMargin+5)-(leftMargin-5),(graphHeight-bottomMargin+5)-(topMargin-5));
	}
	private void drawFrame() {
		Buffer2.setColor(axisColor);
		Buffer2.drawLine(leftMargin-5,topMargin-5,graphWidth-rightMargin+5,topMargin-5);
		Buffer2.drawLine(leftMargin-5,graphHeight-bottomMargin+5,graphWidth-rightMargin+5,graphHeight-bottomMargin+5);
		Buffer2.drawLine(leftMargin-5,topMargin-5,leftMargin-5,graphHeight-bottomMargin+5);
		Buffer2.drawLine(graphWidth-rightMargin+5,topMargin-5,graphWidth-rightMargin+5,graphHeight-bottomMargin+5);
	}
	//graph vizualization (5 level)
	public void plotArray(double[] x, double[] y, Color gr, boolean clean) {
		plotLineStart();
		int ilgis=x.length;
		Buffer2.setColor(gr);
		svgCurrentPolyline="";
		int decimalPlaceX=5;
		if(precissionLabelX-2<0) decimalPlaceX=6-precissionLabelX;
		int decimalPlaceY=5;
		if(precissionLabelY-2<0) decimalPlaceY=6-precissionLabelY;
		int tmp=ilgis-1;
		for(int i=0;i<ilgis;i++) {
			if((xMin<=x[i])&&(xMax>=x[i])&&(yMin<=y[i])&&(yMax>=y[i])) {
				plotLineNext(x[i],y[i],gr,clean);
			}
			if(useSvg) {
				if(i==tmp) svgCurrentPolyline+=(round(x[i],decimalPlaceX)+" "+round(y[i],decimalPlaceY));
				else svgCurrentPolyline+=(round(x[i],decimalPlaceX)+" "+round(y[i],decimalPlaceY)+",");
			}
		}
		svgTemplatePolylines+=("<polyline points=\""+svgCurrentPolyline+"\" stroke=\"rgb("+gr.getRed()+","+gr.getGreen()+","+gr.getBlue()+")\"/>");
		plotLineEnd(gr);
	}
	public void plotArray(double[][] x, Color gr, boolean clean) {
		plotLineStart();
		int ilgis=x.length;
		Buffer2.setColor(gr);
		svgCurrentPolyline="";
		int decimalPlaceX=5;
		if(precissionLabelX-2<0) decimalPlaceX=6-precissionLabelX;
		int decimalPlaceY=5;
		if(precissionLabelY-2<0) decimalPlaceY=6-precissionLabelY;
		int tmp=ilgis-1;
		for(int i=0;i<ilgis;i++) {
			if((xMin<=x[i][0])&&(xMax>=x[i][0])&&(yMin<=x[i][1])&&(yMax>=x[i][1])) {
				plotLineNext(x[i][0],x[i][1],gr,clean);
			}
			if(useSvg) {
				if(i==tmp) svgCurrentPolyline+=(round(x[i][0],decimalPlaceX)+" "+round(x[i][1],decimalPlaceY));
				else svgCurrentPolyline+=(round(x[i][0],decimalPlaceX)+" "+round(x[i][1],decimalPlaceY)+",");
			}
		}
		svgTemplatePolylines+=("<polyline points=\""+svgCurrentPolyline+"\" stroke=\"rgb("+gr.getRed()+","+gr.getGreen()+","+gr.getBlue()+")\"/>");
		plotLineEnd(gr);
	}
	public void plotArrayPoints(double[][] x, Color gr, boolean withstems) {//svg palaikymo nera
		int ilgis=x.length;
		Buffer2.setColor(gr);
		svgCurrentPolyline="";
		int decimalPlaceX=5;
		if(precissionLabelX-2<0) decimalPlaceX=6-precissionLabelX;
		int decimalPlaceY=5;
		if(precissionLabelY-2<0) decimalPlaceY=6-precissionLabelY;
		int tmp=ilgis-1;
		for(int i=0;i<ilgis;i++) {
			if((xMin<=x[i][0])&&(xMax>=x[i][0])&&(yMin<=x[i][1])&&(yMax>=x[i][1])) {
				Buffer2.fillOval(transformX(x[i][0])-2,transformY(x[i][1])-2,4,4);
				if(withstems) Buffer2.drawLine(transformX(x[i][0]),transformY(x[i][1]),transformX(x[i][0]),transformY(Math.max(0,yMin)));
			}
			//if(i==tmp) svgCurrentPolyline+=(round(x[i][0],decimalPlaceX)+" "+round(x[i][1],decimalPlaceY));
			//else svgCurrentPolyline+=(round(x[i][0],decimalPlaceX)+" "+round(x[i][1],decimalPlaceY)+",");
		}
		//svgTemplatePolylines+=("<polyline points=\""+svgCurrentPolyline+"\" stroke=\"rgb("+gr.getRed()+","+gr.getGreen()+","+gr.getBlue()+")\"/>");
	}
	private void plotLineStart() {
		lastPointX=Integer.MIN_VALUE;
		lastPointY=Integer.MIN_VALUE;
	}
	private void plotLineNext(double x, double y, Color gr, boolean clean) {
		if((lastPointX==Integer.MIN_VALUE)&&(lastPointY==Integer.MIN_VALUE)) {
			int x2=transformX(x);
			int y2=transformY(y);
			lastPointX=x2;
			lastPointY=y2;
			plotLine_x=x2;
			plotLine_max=y2;
			plotLine_min=y2;
			plotLine_which=0;
		} else {
			if(clean) {
				int x2=transformX(x);
				int y2=transformY(y);
				if(x2>lastPointX) {
					Buffer2.setColor(gr);
					lastPointX=plotLine_x;
					if(plotLine_which==0) {
						Buffer2.drawLine(lastPointX,lastPointY,plotLine_x,plotLine_max);
						lastPointY=plotLine_max;
					} else if(plotLine_which>0) {
						Buffer2.drawLine(lastPointX,lastPointY,plotLine_x,plotLine_min);
						Buffer2.drawLine(plotLine_x,plotLine_min,plotLine_x,plotLine_max);
						lastPointY=plotLine_max;
					} else {
						Buffer2.drawLine(lastPointX,lastPointY,plotLine_x,plotLine_max);
						Buffer2.drawLine(plotLine_x,plotLine_max,plotLine_x,plotLine_min);
						lastPointY=plotLine_min;
					}
					Buffer2.drawLine(lastPointX,lastPointY,x2,y2);
					lastPointX=x2;
					lastPointY=y2;
					plotLine_x=x2;
					plotLine_max=y2;
					plotLine_min=y2;
					plotLine_which=0;
				} else if(x2==lastPointX) {
					plotLine_x=x2;
					if(plotLine_min>y2) {
						plotLine_min=y2;
						plotLine_which=-1;
					}
					if(plotLine_max<y2) {
						plotLine_max=y2;
						plotLine_which=1;
					}
				}
			} else {
				int x2=transformX(x);
				int y2=transformY(y);
				Buffer2.setColor(gr);
				Buffer2.drawLine(x2,y2,lastPointX,lastPointY);
				lastPointX=x2;
				lastPointY=y2;
			}
		}
	}
	private void plotLineEnd(Color gr) {
		Buffer2.setColor(gr);
		lastPointX=plotLine_x;
		if(plotLine_which==0) {
			Buffer2.drawLine(lastPointX,lastPointY,plotLine_x,plotLine_max);
			lastPointY=plotLine_max;
		} else if(plotLine_which>0) {
			Buffer2.drawLine(lastPointX,lastPointY,plotLine_x,plotLine_min);
			Buffer2.drawLine(plotLine_x,plotLine_min,plotLine_x,plotLine_max);
			lastPointY=plotLine_max;
		} else {
			Buffer2.drawLine(lastPointX,lastPointY,plotLine_x,plotLine_max);
			Buffer2.drawLine(plotLine_x,plotLine_max,plotLine_x,plotLine_min);
			lastPointY=plotLine_min;
		}
		plotLineEnd();
	}
	private void plotLineEnd() {
		lastPointX=Integer.MIN_VALUE;
		lastPointY=Integer.MIN_VALUE;
		plotLine_x=Integer.MIN_VALUE;
		plotLine_max=Integer.MIN_VALUE;
		plotLine_min=Integer.MAX_VALUE;
		plotLine_which=0;
	}
	//rendering (6 level)
	public void renderPlot() {
		Graphics g=getGraphics();
		if(Buffer!=null && g!=null) {
			g.setColor(backgroundColor);
			g.fillRect(0,0,getWidth(),getHeight());
			double aspect=((double)graphWidth)/((double)graphHeight);
			double aspect2=((double)getWidth())/((double)getHeight());
			if(aspect>aspect2) {
				g.drawImage(Buffer,0,0,getWidth(),(int)(getWidth()/aspect), this);
			} else if(aspect<aspect2) {
				g.drawImage(Buffer,0,0,(int)(aspect*getHeight()),getHeight(), this);
			} else {
				g.drawImage(Buffer,0,0,getWidth(),getHeight(), this);
			}
		}
	}
	//output (7 level)
	public void save(String filename) { save(new File(filename)); }
	private void save(File file) {
		   String filename = file.getName();
		String suffix = filename.substring(filename.lastIndexOf('.') + 1);
		   suffix = suffix.toLowerCase();
		try {
			if (suffix.equals("jpg") || suffix.equals("png")) {
				ImageIO.write(Buffer, suffix, file);
			} else if(suffix.equals("svg")) {
				BufferedWriter out=null;
				file.createNewFile();
				out = new BufferedWriter(new FileWriter(file));
				out.write(svgOutput());
				out.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	//inner functions usually used privately
	//	svg output	
	public void setSVGOptions(int d1, int d2) {
		renderSizeX=d1;
		renderSizeY=d2;
	}
	private String svgOutput() {
		String bgColor="rgb("+backgroundColor.getRed()+","+backgroundColor.getGreen()+","+backgroundColor.getBlue()+")";
		String axColor="rgb("+axisColor.getRed()+","+axisColor.getGreen()+","+axisColor.getBlue()+")";
		String grColor="rgb("+gridlineColor.getRed()+","+gridlineColor.getGreen()+","+gridlineColor.getBlue()+")";
		int reducedLeftMargin=leftMargin-5;
		int reducedTopMargin=topMargin-5;		
		int tillBottomMargin=topMargin+renderSizeY+2;
		int tillRightMargin=leftMargin+renderSizeX+2;
		int tillReducedRightMargin=tillRightMargin+5;
		int tillReducedBottomMargin=tillBottomMargin+5;
		double tickSmallTopX1=round(topMargin-4,1);
		double tickSmallTopX2=round(topMargin-2.5,1);
		double tickSmallBottomX1=round(tillBottomMargin+2.5,1);
		double tickSmallBottomX2=round(tillBottomMargin+4,1);
		double tickSmallLeftX1=round(leftMargin-4,1);
		double tickSmallLeftX2=round(leftMargin-2.5,1);
		double tickSmallRightX1=round(tillRightMargin+2.5,1);
		double tickSmallRightX2=round(tillRightMargin+4,1);
		double bottomFontMargin=tillBottomMargin+6+fontSize;
		double leftFontMargin=leftMargin-6;
		int amountX=ticksXCoord.length;
		int xPos=0;
		int decimalPlaceX=1;
		if(precissionLabelX-2<0) decimalPlaceX=2-precissionLabelX;
		int smallerDecimalPlaceX=decimalPlaceX+4;
		int decimalPlaceY=1;
		if(precissionLabelY-2<0) decimalPlaceY=2-precissionLabelY;
		int smallerDecimalPlaceY=decimalPlaceY+4;
		String script="";
		String rez="<?xml version=\"1.0\" encoding=\"UTF-8\"?>\r\n<!DOCTYPE svg PUBLIC \"-//W3C//DTD SVG 1.0//EN\" \"http://www.w3.org/TR/2001/REC-SVG-20010904/DTD/svg10.dtd\">\r\n<svg xmlns=\"http://www.w3.org/2000/svg\" xmlns:xlink=\"http://www.w3.org/1999/xlink\" width=\""+(renderSizeX+enhancedLeftMargin+rightMargin)+"\" height=\""+(renderSizeY+enhancedTopMargin+enhancedBottomMargin)+"\" viewBox=\"0 0 "+(renderSizeX+enhancedLeftMargin+rightMargin)+" "+(renderSizeY+enhancedTopMargin+enhancedBottomMargin)+"\">\r\n";
		//plot label
		rez+="<rect x=\"0\" y=\"0\" width=\"100%\" height=\"100%\" fill=\""+bgColor+"\"/>\r\n";
		if(usePlotLabel) {
			rez+="<text x=\""+(leftMargin+renderSizeX/2)+"\" y=\""+(topMargin/2)+"\" text-anchor=\"middle\" font-family=\""+fontName+"\" font-size=\""+fontSize+"\" fill=\""+axColor+"\" stroke=\"none\">"+labelToPlot+"</text>\r\n";
		}
		//points ploted
		rez+="<svg x=\""+(enhancedLeftMargin)+"\" y=\""+(enhancedTopMargin)+"\" width=\""+renderSizeX+"\" height=\""+renderSizeY+"\" viewBox=\""+round(xMin,smallerDecimalPlaceX)+" "+round(yMin,smallerDecimalPlaceY)+" "+round(xDif,smallerDecimalPlaceX)+" "+round(yDif,smallerDecimalPlaceY)+"\" preserveAspectRatio=\"none\">\r\n";
		rez+="<g stroke-width=\""+(Math.max(Math.abs(xDif),Math.abs(yDif))/700)+"\" fill=\"none\" transform=\"translate(0,"+round(yMin+yMax,smallerDecimalPlaceY)+") scale(1,-1)\">\r\n";
		rez+=svgTemplatePolylines;
		rez+="</g></svg>\r\n";		
		//gridLines
		if(plotGridlines) {
			rez+="<g stroke=\""+grColor+"\" stroke-width=\"1\" fill=\"none\">\r\n";
			amountX=ticksXCoord.length;
			xPos=0;
			for(int i=0;i<amountX;i++) {
				xPos=(int)Math.floor(enhancedLeftMargin+renderSizeX*ticksXCoord[i]);
				rez+="<line x1=\""+xPos+"\" y1=\""+(reducedTopMargin)+"\" x2=\""+xPos+"\" y2=\""+(tillReducedBottomMargin)+"\"/>\r\n";
			}
			amountX=ticksYCoord.length;
			for(int i=0;i<amountX;i++) {
				xPos=(int)Math.floor(enhancedTopMargin+renderSizeY*(1-ticksYCoord[i]));
				rez+="<line y1=\""+xPos+"\" x1=\""+(reducedLeftMargin)+"\" y2=\""+xPos+"\" x2=\""+(tillReducedRightMargin)+"\"/>\r\n";
			}
			rez+="</g>\r\n";
		}
		//graph tics
		rez+="<g stroke=\""+axColor+"\" stroke-width=\"1\" fill=\"none\" font-family=\""+fontName+"\" font-size=\""+fontSize+"\">\r\n";
		amountX=ticksXCoord.length;
		xPos=0;
		for(int i=0;i<amountX;i++) {
			xPos=(int)Math.floor(enhancedLeftMargin+renderSizeX*ticksXCoord[i]);
			rez+="<line x1=\""+xPos+"\" y1=\""+(reducedTopMargin)+"\" x2=\""+xPos+"\" y2=\""+(topMargin)+"\"/>\r\n";
			rez+="<line x1=\""+xPos+"\" y1=\""+(tillBottomMargin)+"\" x2=\""+xPos+"\" y2=\""+(tillReducedBottomMargin)+"\"/>\r\n";
		}
		amountX=ticksYCoord.length;
		for(int i=0;i<amountX;i++) {
			xPos=(int)Math.floor(enhancedTopMargin+renderSizeY*(1-ticksYCoord[i]));
			rez+="<line y1=\""+xPos+"\" x1=\""+(reducedLeftMargin)+"\" y2=\""+xPos+"\" x2=\""+(leftMargin)+"\"/>\r\n";
			rez+="<line y1=\""+xPos+"\" x1=\""+(tillRightMargin)+"\" y2=\""+xPos+"\" x2=\""+(tillReducedRightMargin)+"\"/>\r\n";
		}
		amountX=smallTicksXCoord.length;
		xPos=0;
		for(int i=0;i<amountX;i++) {
			xPos=(int)Math.floor(enhancedLeftMargin+renderSizeX*smallTicksXCoord[i]);
			rez+="<line x1=\""+xPos+"\" y1=\""+(tickSmallTopX1)+"\" x2=\""+xPos+"\" y2=\""+(tickSmallTopX2)+"\"/>\r\n";
			rez+="<line x1=\""+xPos+"\" y1=\""+(tickSmallBottomX1)+"\" x2=\""+xPos+"\" y2=\""+(tickSmallBottomX2)+"\"/>\r\n";
		}
		amountX=smallTicksYCoord.length;
		for(int i=0;i<amountX;i++) {
			xPos=(int)Math.floor(enhancedTopMargin+renderSizeY*(1-smallTicksYCoord[i]));
			rez+="<line y1=\""+xPos+"\" x1=\""+(tickSmallLeftX1)+"\" y2=\""+xPos+"\" x2=\""+(tickSmallLeftX2)+"\"/>\r\n";
			rez+="<line y1=\""+xPos+"\" x1=\""+(tickSmallRightX1)+"\" y2=\""+xPos+"\" x2=\""+(tickSmallRightX2)+"\"/>\r\n";
		}
		rez+="</g>";
		//graph frame
		rez+="<g stroke=\""+axColor+"\" stroke-width=\"1\" fill=\"none\">\r\n";
		rez+="<line x1=\""+(reducedLeftMargin)+"\" y1=\""+(reducedTopMargin)+"\" x2=\""+(tillReducedRightMargin)+"\" y2=\""+(reducedTopMargin)+"\"/>\r\n";
		rez+="<line x1=\""+(reducedLeftMargin)+"\" y1=\""+(tillReducedBottomMargin)+"\" x2=\""+(tillReducedRightMargin)+"\" y2=\""+(tillReducedBottomMargin)+"\"/>\r\n";
		rez+="<line x1=\""+(reducedLeftMargin)+"\" y1=\""+(reducedTopMargin)+"\" x2=\""+(reducedLeftMargin)+"\" y2=\""+(tillReducedBottomMargin)+"\"/>\r\n";
		rez+="<line x1=\""+(tillReducedRightMargin)+"\" y1=\""+(reducedTopMargin)+"\" x2=\""+(tillReducedRightMargin)+"\" y2=\""+(tillReducedBottomMargin)+"\"/>\r\n";
		rez+="</g>\r\n";
		//labels
		rez+="<g stroke=\"none\" fill=\""+axColor+"\" font-family=\""+fontName+"\" font-size=\""+fontSize+"\">\r\n";
		amountX=ticksXCoord.length;
		xPos=0;
		for(int i=0;i<amountX;i++) {
			xPos=(int)Math.floor(leftMargin+renderSizeX*ticksXCoord[i]);
			rez+="<text text-anchor=\"middle\" x=\""+xPos+"\" y=\""+(bottomFontMargin)+"\">"+ticksXLabel[i]+"</text>\r\n";
		}
		amountX=ticksYCoord.length;
		for(int i=0;i<amountX;i++) {
			xPos=(int)Math.floor(topMargin+renderSizeY*(1-ticksYCoord[i])+fontSize/2.0);
			rez+="<text text-anchor=\"end\" y=\""+xPos+"\" x=\""+(leftFontMargin)+"\">"+ticksYLabel[i]+"</text>\r\n";
		}
		rez+="</g>\r\n";
		rez+="</svg>\r\n";
		return rez;
	}
	//	functions involved with decimal places
	private double smallestScaleIncrement(int decimalPlace) {
		double multiplier=1;
		if(decimalPlace>0) {
			while(decimalPlace>0) {
				decimalPlace--;
				multiplier*=10;
			}
		} else if(decimalPlace<0) {
			while(decimalPlace<0) {
				decimalPlace++;
				multiplier/=10;
			}
		}
		return multiplier;
	}
	private double roundToMultiplier(double a1, double multiplier) {
		return Math.floor(a1/multiplier)*multiplier;
	}
	private int getDigitScale(double a1, boolean xScale) {
		int rez=0;
		a1=Math.abs(a1);
		if(a1>=10) {
			while(a1>=10) {
				a1/=10;
				rez++;
			}
		} else if(a1<=1) {
			while(a1<=1) {
				a1*=10;
				rez--;
			}
		}
		/*if(xScale) useSmallTicksX=true;
		else useSmallTicksY=true;
		if(a1<2) {
			rez--;
			if(xScale) useSmallTicksX=false;
			else useSmallTicksY=false;
		}*/
		return rez;
	}
	private double round(double d, int i) {
		int j = i;
		BigDecimal bigdecimal = new BigDecimal(d);
		bigdecimal = bigdecimal.setScale(j,bigdecimal.ROUND_HALF_UP);
		d = bigdecimal.doubleValue();
		return d;
	}
	//tranformation from ploting scale coordinates to graph coordinates	
	private int transformX(double x) {
		return enhancedLeftMargin+(int)(((x-xMin)/xDif)*distanceXInPx);
	}
	private int transformY(double y) {
		return graphHeight-enhancedBottomMargin-(int)(((y-yMin)/yDif)*distanceYInPx);
	}
}
