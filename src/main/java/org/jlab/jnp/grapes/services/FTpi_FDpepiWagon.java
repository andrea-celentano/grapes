package org.jlab.jnp.grapes.services;

import org.jlab.jnp.hipo4.data.*;
import org.jlab.jnp.hipo.data.HipoNode;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * 
 * Skim for pi+/pi- in FT, p+e+pi in FD+CD 
 *
 * @author celentano
 */

public class FTpi_FDpepiWagon extends Wagon {

	public FTpi_FDpepiWagon(){
        super("FTpi_FDpepiWagon","devita","0.1");
    }

	@Override
	public boolean init(String jsonString) {
		System.out.println("FTpi_FDpepiWagon READY.");
		return true;
	}

	@Override
	public boolean processDataEvent(Event event, SchemaFactory factory) {

//		Bank bank = new Bank(factory.getSchema("REC::Particle"));
//		Bank bankRECFT = new Bank(factory.getSchema("RECFT::Particle"));
		Bank bankREC   = new Bank(factory.getSchema("REC::Particle"));

//		event.read(bank);

//		event.read(bankRECFT);
		event.read(bankREC);

		boolean flag_saveMe = false;
		int     nel  = 0;
		
                int     npos_FD = 0;
                int     nneg_FD = 0;
		int	nprot_FD=0;
		int 	npip_FD=0;
		int 	npim_FD=0;
		
                int     npos_CD = 0;
                int     nneg_CD = 0;
		int	nprot_CD=0;
		int 	npip_CD=0;
		int 	npim_CD=0;
		
		
		int nChargeFT=0;

                if(bankREC!=null) {
                    for (int ii = 0; ii < bankREC.getRows(); ii++) {
                            int pid    = bankREC.getInt("pid", ii);
                            int charge = bankREC.getByte("charge", ii);
                            double  px = bankREC.getFloat("px", ii);
                            double  py = bankREC.getFloat("py", ii);
                            double  pz = bankREC.getFloat("pz", ii);
                            double   p = Math.sqrt(px*px+py*py+pz*pz);
			    double chi2 = bankREC.getFloat("chi2pid",ii);
			    int status = bankREC.getShort("status", ii);

                            if (pid == 11 && status>-4000 && status<-2000 && p>1) nel++; 
                            if ((status>2000)&&(status<4000)) {
                                if (charge >0)    npos_FD++;
                                else if(charge<0) nneg_FD++;
				if (Math.abs(chi2)<5){
				    switch(pid){
				    case 211:
					npip_FD++;
					break;
				    case -211:
					npim+_FD+;
					break;
				    case 2212:
					nprot_FD++;
					break;
				    }
				}
			    }
			    if ((status>4000)&&(status<8000)) {
                                if (charge >0)    npos_CD++;
                                else if(charge<0) nneg_CD++;
				if (Math.abs(chi2)<5){
				    switch(pid){
				    case 211:
					npip_CD++;
					break;
				    case -211:
					npim+_CD+;
					break;
				    case 2212:
					nprot_CD++;
					break;
				    }
				}
			    }
			    			    
			    if ((status>1000)&&(status<2000)) {
				if (charge !=0) nChargeFT++; 
			    }
                    }
			
			
		   int nprot=nprot_CD+nprot_FD;
		   int npip=npip_CD+npip_FD;
		   int npim=npim_CD+npim_FD;
		   int npos=npos_CD+npos_FD;
		   int nneg=nneg_CD+nneg_FD;	
			
		    if (nel==1 && (npos+nneg)>=2 && (npos+nneg)<=8 && nprot==1 && nChargeFT==1 && (npip+npim)==1)  flag_saveMe=true;
                }
                
                return flag_saveMe;
	}

	private HashMap<Integer, ArrayList<Integer>> mapByIndex(HipoNode indices) {
		HashMap<Integer, ArrayList<Integer>> map = new HashMap<Integer, ArrayList<Integer>>();
		for (int ii = 0; ii < indices.getDataSize(); ii++) {
			final int index = indices.getInt(ii);
			if (!map.containsKey(index))
				map.put(index, new ArrayList<Integer>());
			map.get(index).add(ii);
		}
		return map;
	}

}
