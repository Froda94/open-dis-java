package edu.nps.moves.deadreckoning;

import org.apache.commons.math3.geometry.euclidean.threed.Rotation;
import org.apache.commons.math3.geometry.euclidean.threed.RotationConvention;
import org.apache.commons.math3.geometry.euclidean.threed.RotationOrder;
import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;
import org.apache.commons.math3.linear.MatrixUtils;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;

import edu.nps.moves.deadreckoning.utils.*;

/**
 * 
 * (SECONDARY Methods Group) Rotating, rate of position, body coordinates || 
 * Linear motion with Rotation
 * <p>
 * it is coded up, but the linear motion does not seem to work....rotation
 * works but linear motion fails...not sure why they are calculating the 
 * linear motion the way they are...
 * <p>
 * The alogrithm is coded IAW IEEE 1278.1-1995 so perhaps it is a 
 * coordinate change of basis issue and since I am not working in both world
 * and body coordinates, it fails or limits to 0
 * 
 * @author Sheldon L. Snyder
 */
public class DIS_DR_RPB_07 extends DIS_DeadReckoning
{
    RealMatrix DR;
    RealMatrix R1;

    /**
     * The driver for a DIS_DR_RPB_07 DR algorithm from the Runnable interface
     * <p>
     * linear motion and rotation
     */
    public void run()
    {
        try
        {            
            while(true)
            {
                Thread.sleep(stall);    
                update();
            }//while(true)  
        }// try
        catch(Exception e)
        {   
            System.out.println(e);     
        }
    }//run()--------------------------------------------------------------------

    void update() throws MatrixException, Exception {
        deltaCt++;

        // solve for the new position
        makeR1();
        System.out.println("R1:");
        for (int i = 0; i < 3; i++) {
            System.out.println(R1.getEntry(i, 0) + " " + R1.getEntry(i, 1) + " " + R1.getEntry(i, 2));
        }

        RealVector velVec = MatrixUtils.createRealVector(new double[]
                {entityLinearVelocity_X, entityLinearVelocity_Y, entityLinearVelocity_Z});
        Vector3D updated = initOrien.applyInverseTo(new Vector3D(R1.operate(velVec).toArray()));

        // set new positons
        entityLocation_X += updated.getX();
        entityLocation_Y += updated.getY();
        entityLocation_Z += updated.getZ();                

        // make the rotation same as 1-4 rotations
        makeThisDR();                
        Rotation DRR = new Rotation(DR.getData(), 1e-15).applyTo(initOrien);

        double[] eulerAngles = DRR.getAngles(RotationOrder.ZYX, RotationConvention.FRAME_TRANSFORM);
        // Update ESPDU Euler angle values

        entityOrientation_theta = (float) eulerAngles[1];   
        //System.out.println(entityOrientation_theta);
        entityOrientation_psi = (float) eulerAngles[0];
        entityOrientation_phi = (float) eulerAngles[2];

        if(Double.isNaN(entityOrientation_psi))
            entityOrientation_psi = 0;
        if(Double.isNaN(entityOrientation_theta))
            entityOrientation_theta = 0;
        if(Double.isNaN(entityOrientation_phi))
            entityOrientation_phi = 0;                
    }


    /***************************************************************************
     * Makes this iterations DR matrix
     * @throws java.lang.Exception
     */
    private void makeThisDR() throws Exception
    {
        double wDelta = wMag * changeDelta * deltaCt;  
        double cosWdelta = Math.cos(wDelta);

        double wwScale = (1 - cosWdelta) / wSq; 
        double identScalar = cosWdelta;
        double skewScale = Math.sin(wDelta) / wMag;

        RealMatrix wwTmp = ww.scalarMultiply(wwScale);
        RealMatrix identTmp = MatrixUtils.createRealIdentityMatrix(3).scalarMultiply(identScalar);
        RealMatrix skwTmp = skewOmega.scalarMultiply(skewScale);

        DR = wwTmp.add(identTmp);
        DR = DR.subtract(skwTmp);
    }//makeThisDR() throws Exception--------------------------------------------


    /***************************************************************************
     * Makes the R1 matrix
     * @return - the vector R1
     * @throws java.lang.Exception
     */
    private void makeR1() throws Exception
    {
        RealMatrix ident = MatrixUtils.createRealIdentityMatrix(3);  

        // common factors
        double wDelta = wMag * changeDelta * deltaCt;  

        // matrix scalars
        double wwScale = (wDelta-Math.sin(wDelta)) / (wSq * wMag); 
        double identScalar = Math.sin(wDelta) / wMag;
        double skewScale = (1.0 - Math.cos(wDelta)) / wSq;

        System.out.println("wwScale: " + wwScale);
        System.out.println("identScalar: " + identScalar);
        System.out.println("skewScale: " + skewScale);

        // scaled matrixes
        RealMatrix wwTmp = ww.scalarMultiply(wwScale);
        RealMatrix identTmp = ident.scalarMultiply(identScalar);
        RealMatrix skwTmp = skewOmega.scalarMultiply(skewScale);

        R1 = wwTmp.add(identTmp);
        R1 = R1.add(skwTmp);
    }//makeR1() throws Exception------------------------------------------------


}