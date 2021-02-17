package decodeavro;

// By A.C. Cop. PoC that we can deserialize our original confluent kafka avro messages back from azure event blob storage.

import java.io.File;
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

public class App {
    public static void main(String[] args) {
        try {
            deserializeBlob("/home/allan/good-things/avrodecode/java/decodeavro/38.avro", "/home/allan/good-things/avrodecode/java/decodeavro/38.avsc");
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }
    public static String deserializeByteArray(String message, String schema) throws IOException {
        Schema.Parser schemaParser = new Schema.Parser();
        Schema avroSchema = schemaParser.parse(schema);
        byte[] valueAsBytes = message.getBytes(StandardCharsets.ISO_8859_1);
        int valueByteLength = valueAsBytes.length;
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

    public static void deserializeBlob(String file, String sourceSystemSchema) throws IOException {
        DatumReader<GenericRecord> userDatumReader = new SpecificDatumReader<GenericRecord>();
        DataFileReader<GenericRecord> dataFileReader = new DataFileReader<GenericRecord>(new File(file), userDatumReader);
        Object record = null;
        while (dataFileReader.hasNext()) {
        record = dataFileReader.next();
            JSONObject jObject = new JSONObject(record.toString());
            String messageString = jObject.getJSONObject("Body").getString("bytes");
            String schema = readFile(sourceSystemSchema);
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
