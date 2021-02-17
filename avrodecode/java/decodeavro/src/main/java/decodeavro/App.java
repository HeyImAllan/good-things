package decodeavro;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

import org.json.JSONObject;
import org.apache.avro.Schema;
import org.apache.avro.file.DataFileReader;
import org.apache.avro.generic.GenericDatumReader;
import org.apache.avro.generic.GenericRecord;
import org.apache.avro.io.DatumReader;
import org.apache.avro.io.Decoder;
import org.apache.avro.io.DecoderFactory;
import org.apache.avro.specific.SpecificDatumReader;

/**
 * Hello world!
 *
 */
public class App {
    public static void main(String[] args) {
        String message = new String();
        String schema = new String();
        try {
            message = readFile("/home/allan/good-things/avrodecode/java/decodeavro/38.avro");
            schema = readFile("/home/allan/good-things/avrodecode/java/decodeavro/38.avsc");
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        // System.out.println(message + schema);

        String blobOutput = new String();
        try {
            deserializeFile("/home/allan/good-things/avrodecode/java/decodeavro/38.avro");
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        System.out.println(blobOutput);

    }

    // public static String deserialize(String message, String schema) throws IOException {
    //     Schema.Parser schemaParser = new Schema.Parser();
    //     Schema avroSchema = schemaParser.parse(schema);

    //     DatumReader<GenericRecord> specificDatumReader = new SpecificDatumReader<GenericRecord>(avroSchema);

    //     byte[] messageBytes = message.getBytes();
    //     Decoder decoder = DecoderFactory.get().binaryDecoder(messageBytes, null);

    //     Object genericRecord = specificDatumReader.read(null, decoder);

    //     return genericRecord.toString();
    // }

    // public static String deserialize(String message) throws IOException {
    // // Schema.Parser schemaParser = new Schema.Parser();
    // // Schema avroSchema = schemaParser.parse(schema);

    // DatumReader<GenericRecord> specificDatumReader = new
    // SpecificDatumReader<GenericRecord>();

    // byte[] messageBytes = message.getBytes();
    // Decoder decoder = DecoderFactory.get().binaryDecoder(messageBytes, null);
    // // GenericRecord genericRecord = new GenericData.Record(decoder.getSchema())
    // Object datum = specificDatumReader.read(null, decoder);
    // System.out.println("i passed here");
    // return datum.toString();
    // }
    public static String deserializeByteArray(String message, String schema) throws IOException {
        Schema.Parser schemaParser = new Schema.Parser();
        Schema avroSchema = schemaParser.parse(schema);
        System.out.println(message);
        byte[] valueAsBytes = message.getBytes(StandardCharsets.UTF_8);
        int valueByteLength = valueAsBytes.length;
        //System.out.println(valueAsBytes);
        //System.out.println(valueByteLength);
        ByteBuffer b = ByteBuffer.wrap(valueAsBytes);
        byte MAGIC_BYTE = (byte) 0x0;
        if (b.get() == MAGIC_BYTE) {
            int sourceSchemaId = b.getInt();
            System.out.println(sourceSchemaId);
            byte[] arr2 = Arrays.copyOfRange(valueAsBytes, 5, valueByteLength);
            GenericDatumReader<GenericRecord> datumReader = new GenericDatumReader<GenericRecord>(avroSchema);
            Decoder decoder = DecoderFactory.get().binaryDecoder((byte[]) arr2, null);
            Object avroDatum = datumReader.read(null, decoder);
            return avroDatum.toString();
        }
        return null;  
    }

    public static void deserializeFile(String file) throws IOException {
        // Deserialize Users from disk
        DatumReader<GenericRecord> userDatumReader = new SpecificDatumReader<GenericRecord>();
        DataFileReader<GenericRecord> dataFileReader = new DataFileReader<GenericRecord>(new File(file), userDatumReader);
        Object record = null;
        while (dataFileReader.hasNext()) {
        // Reuse user object by passing it to next(). This saves us from
        // allocating and garbage collecting many objects for files with
        // many items.
        record = dataFileReader.next();
            // System.out.println(record);
            // System.out.println(record.getClass().getName());
            // System.out.println(record.toString());
            JSONObject jObject = new JSONObject(record.toString());
            // System.out.println(jObject.getJSONObject("Body"));
            String messageString = jObject.getJSONObject("Body").getString("bytes");
            String schema = readFile("/home/allan/good-things/avrodecode/java/decodeavro/38.avsc");
            String myout = deserializeByteArray(messageString, schema);
            System.out.println(myout);
        }

    }

    public static String readFile(String filename) throws IOException {
        String content = null;
        File file = new File(filename); // For example, foo.txt
        FileReader reader = null;
        try {
            reader = new FileReader(file);
            char[] chars = new char[(int) file.length()];
            reader.read(chars);
            content = new String(chars);
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                reader.close();
            }
        }
        return content;
    }
}
