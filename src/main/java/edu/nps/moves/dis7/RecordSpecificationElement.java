package edu.nps.moves.dis7;

import java.util.*;
import java.io.*;
import edu.nps.moves.disenum.*;
import edu.nps.moves.disutil.*;

// Jaxb and Hibernate annotations generally won't work on mobile devices. XML serialization uses jaxb, and
// javax.persistence uses the JPA JSR, aka hibernate. See the Hibernate site for details.
// To generate Java code without these, and without the annotations scattered through the
// see the XMLPG java code generator, and set the boolean useHibernateAnnotations and useJaxbAnnotions 
// to false, and then regenerate the code

import javax.xml.bind.*;            // Used for JAXB XML serialization
import javax.xml.bind.annotation.*; // Used for XML serialization annotations (the @ stuff)
import javax.persistence.*;         // Used for JPA/Hibernate SQL persistence

/**
 * Synthetic record, made up from section 6.2.72. This is used to acheive a repeating variable list element.
 *
 * Copyright (c) 2008-2014, MOVES Institute, Naval Postgraduate School. All rights reserved.
 * This work is licensed under the BSD open source license, available at https://www.movesinstitute.org/licenses/bsd.html
 *
 * @author DMcG
 */
@Entity  // Hibernate
@Inheritance(strategy=InheritanceType.JOINED)  // Hibernate
public class RecordSpecificationElement extends Object implements Serializable
{
   /** Primary key for hibernate, not part of the DIS standard */
   private long pk_RecordSpecificationElement;

   /** the data structure used to convey the parameter values of the record for each record. 32 bit enumeration. */
   protected long  recordID;

   /** the serial number of the first record in the block of records */
   protected long  recordSetSerialNumber;

   /**  the length, in bits, of the record. Note, bits, not bytes. */
   protected int  recordLength;

   /**  the number of records included in the record set  */
   protected int  recordCount;

   /** the concatenated records of the format specified by the Record ID field. The length of this field is the Record Length multiplied by the Record Count, in units of bits. ^^^This is wrong--variable sized data records, bit values. THis MUST be patched after generation. */
   protected int  recordValues;

   /** Padding of 0 to 31 unused bits as required for 32-bit alignment of the Record Set field. ^^^This is wrong--variable sized padding. MUST be patched post-code generation */
   protected short  pad4;


/** Constructor */
 public RecordSpecificationElement()
 {
 }

@Transient  // Marked as transient to prevent hibernate from thinking this is a persistent property
public int getMarshalledSize()
{
   int marshalSize = 0; 

   marshalSize = marshalSize + 4;  // recordID
   marshalSize = marshalSize + 4;  // recordSetSerialNumber
   marshalSize = marshalSize + 2;  // recordLength
   marshalSize = marshalSize + 2;  // recordCount
   marshalSize = marshalSize + 2;  // recordValues
   marshalSize = marshalSize + 1;  // pad4

   return marshalSize;
}


/** Primary key for hibernate, not part of the DIS standard */
@Id
@GeneratedValue(strategy=GenerationType.AUTO)
public long getPk_RecordSpecificationElement()
{
   return pk_RecordSpecificationElement;
}

/** Hibernate primary key, not part of the DIS standard */
public void setPk_RecordSpecificationElement(long pKeyName)
{
   this.pk_RecordSpecificationElement = pKeyName;
}

public void setRecordID(long pRecordID)
{ recordID = pRecordID;
}

@XmlAttribute // Jaxb
@Basic       // Hibernate
public long getRecordID()
{ return recordID; 
}

public void setRecordSetSerialNumber(long pRecordSetSerialNumber)
{ recordSetSerialNumber = pRecordSetSerialNumber;
}

@XmlAttribute // Jaxb
@Basic       // Hibernate
public long getRecordSetSerialNumber()
{ return recordSetSerialNumber; 
}

public void setRecordLength(int pRecordLength)
{ recordLength = pRecordLength;
}

@XmlAttribute // Jaxb
@Basic       // Hibernate
public int getRecordLength()
{ return recordLength; 
}

public void setRecordCount(int pRecordCount)
{ recordCount = pRecordCount;
}

@XmlAttribute // Jaxb
@Basic       // Hibernate
public int getRecordCount()
{ return recordCount; 
}

public void setRecordValues(int pRecordValues)
{ recordValues = pRecordValues;
}

@XmlAttribute // Jaxb
@Basic       // Hibernate
public int getRecordValues()
{ return recordValues; 
}

public void setPad4(short pPad4)
{ pad4 = pPad4;
}

@XmlAttribute // Jaxb
@Basic       // Hibernate
public short getPad4()
{ return pad4; 
}


public void marshal(DataOutputStream dos)
{
    try 
    {
       dos.writeInt( (int)recordID);
       dos.writeInt( (int)recordSetSerialNumber);
       dos.writeShort( (short)recordLength);
       dos.writeShort( (short)recordCount);
       dos.writeShort( (short)recordValues);
       dos.writeByte( (byte)pad4);
    } // end try 
    catch(Exception e)
    { 
      System.out.println(e);}
    } // end of marshal method

public void unmarshal(DataInputStream dis)
{
    try 
    {
       recordID = dis.readInt();
       recordSetSerialNumber = dis.readInt();
       recordLength = (int)dis.readUnsignedShort();
       recordCount = (int)dis.readUnsignedShort();
       recordValues = (int)dis.readUnsignedShort();
       pad4 = (short)dis.readUnsignedByte();
    } // end try 
   catch(Exception e)
    { 
      System.out.println(e); 
    }
 } // end of unmarshal method 


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
       buff.putInt( (int)recordID);
       buff.putInt( (int)recordSetSerialNumber);
       buff.putShort( (short)recordLength);
       buff.putShort( (short)recordCount);
       buff.putShort( (short)recordValues);
       buff.put( (byte)pad4);
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
       recordID = buff.getInt();
       recordSetSerialNumber = buff.getInt();
       recordLength = (int)(buff.getShort() & 0xFFFF);
       recordCount = (int)(buff.getShort() & 0xFFFF);
       recordValues = (int)(buff.getShort() & 0xFFFF);
       pad4 = (short)(buff.get() & 0xFF);
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

    if(!(obj instanceof RecordSpecificationElement))
        return false;

     final RecordSpecificationElement rhs = (RecordSpecificationElement)obj;

     if( ! (recordID == rhs.recordID)) ivarsEqual = false;
     if( ! (recordSetSerialNumber == rhs.recordSetSerialNumber)) ivarsEqual = false;
     if( ! (recordLength == rhs.recordLength)) ivarsEqual = false;
     if( ! (recordCount == rhs.recordCount)) ivarsEqual = false;
     if( ! (recordValues == rhs.recordValues)) ivarsEqual = false;
     if( ! (pad4 == rhs.pad4)) ivarsEqual = false;

    return ivarsEqual;
 }
} // end of class