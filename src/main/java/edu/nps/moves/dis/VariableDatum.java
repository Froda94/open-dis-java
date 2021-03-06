package edu.nps.moves.dis;

import java.util.*;
import java.io.*;


/**
 * Section 5.2.32. Variable Datum Record
 *
 * Copyright (c) 2008-2016, MOVES Institute, Naval Postgraduate School. All rights reserved.
 * This work is licensed under the BSD open source license, available at https://www.movesinstitute.org/licenses/bsd.html
 *
 * @author DMcG
 */
public class VariableDatum extends Object implements Serializable
{
   /** ID of the variable datum */
   protected long  variableDatumID;

   /** length of the variable datums, in bits. Note that this is not programmatically tied to the size of the variableData. The variable data field may be 64 bits long but only 16 bits of it could actually be used. */
   protected long  variableDatumLength;

   /** data can be any length, but must increase in 8 byte quanta. This requires some postprocessing patches. Note that setting the data allocates a new internal array to account for the possibly increased size. The default initial size is 64 bits. */
   protected byte[] variableData;

/** Constructor */
 public VariableDatum()
 {
 }

public int getMarshalledSize()
{
   int marshalSize = 0; 

   marshalSize = marshalSize + 4;  // variableDatumID
   marshalSize = marshalSize + 4;  // variableDatumLength
   marshalSize = marshalSize + variableData.length;

   // Account for required padding.
   marshalSize = marshalSize + datumPaddingSize();

   return marshalSize;
}


public void setVariableDatumID(long pVariableDatumID)
{ variableDatumID = pVariableDatumID;
}

public long getVariableDatumID()
{ return variableDatumID; 
}

public long getVariableDatumLength()
{ return (long)variableData.length * Byte.SIZE;
}

/** Note that setting this value will not change the marshalled value. The list whose length this describes is used for that purpose.
 * The getvariableDatumLength method will also be based on the actual list length rather than this value. 
 * The method is simply here for java bean completeness.
 */
public void setVariableDatumLength(long pVariableDatumLength)
{ variableDatumLength = pVariableDatumLength;
}

public void setVariableData(byte[] pVariableData)
{ variableData = pVariableData;
}

public byte[] getVariableData()
{ return variableData; }


/**
 * Packs a Pdu into the ByteBuffer.
 * @throws java.nio.BufferOverflowException if buff is too small
 * @throws java.nio.ReadOnlyBufferException if buff is read only
 * @see java.nio.ByteBuffer
 * @param buff The ByteBuffer at the position to begin writing
 * @since ??
 */
public void marshal(java.nio.ByteBuffer buff)
{
       buff.putInt( (int)variableDatumID);
       buff.putInt( (int)getVariableDatumLength());
       buff.put(variableData);

        // Add padding.
        for (int i = 0; i < datumPaddingSize(); i++) {
            buff.put((byte) 0);
        }
    } // end of marshal method

/**
 * Unpacks a Pdu from the underlying data.
 * @throws java.nio.BufferUnderflowException if buff is too small
 * @see java.nio.ByteBuffer
 * @param buff The ByteBuffer at the position to begin reading
 * @since ??
 */
public void unmarshal(java.nio.ByteBuffer buff)
{
       variableDatumID = buff.getInt();
       variableDatumLength = buff.getInt();
       final int dataLengthBytes = (int)variableDatumLength / Byte.SIZE;
       variableData = new byte[dataLengthBytes];
       buff.get(variableData);
       buff.position(buff.position() + calculatePaddingSize(dataLengthBytes)); // skip padding

 } // end of unmarshal method 


 /*
  * The equals method doesn't always work--mostly it works only on classes that consist only of primitives. Be careful.
  */
@Override
 public boolean equals(Object obj)
 {

    if(this == obj){
      return true;
    }

    if(obj == null){
       return false;
    }

    if(getClass() != obj.getClass())
        return false;

    return equalsImpl(obj);
 }

 /**
  * Compare all fields that contribute to the state, ignoring
 transient and static fields, for <code>this</code> and the supplied object
  * @param obj the object to compare to
  * @return true if the objects are equal, false otherwise.
  */
 public boolean equalsImpl(Object obj)
 {
     boolean ivarsEqual = true;

    if(!(obj instanceof VariableDatum))
        return false;

     final VariableDatum rhs = (VariableDatum)obj;

     if( ! (variableDatumID == rhs.variableDatumID)) ivarsEqual = false;
     if( ! (variableDatumLength == rhs.variableDatumLength)) ivarsEqual = false;
     if( ! (Arrays.equals(variableData, rhs.variableData))) ivarsEqual = false;

    return ivarsEqual;
 }
    
    // "This field shall be padded at the end to make the length a multiple of 64-bits."
    private int datumPaddingSize() {
        return calculatePaddingSize(variableData.length);
    }
    
    private static int calculatePaddingSize(int datumLength) {
        final int BYTES_IN_64_BITS = 8;
        int padding = 0;
        final int remainder = datumLength % BYTES_IN_64_BITS;
        if (remainder != 0) {
            padding = BYTES_IN_64_BITS - remainder;
        }
        return padding;
    }

} // end of class
