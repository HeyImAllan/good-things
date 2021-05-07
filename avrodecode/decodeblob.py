#!/usr/bin/python3
# Python 3 with `avro-python3` package available
import copy
import simplejson as json
import avro
import io
import fastavro
from avro.datafile import DataFileWriter, DataFileReader
from avro.io import DatumWriter, DatumReader, BinaryDecoder

# Read data from an avro file
with open('/Users/cv65ry/Projects/good-things/avrodecode/38.avro', 'rb') as f:
    reader = DataFileReader(f, DatumReader())
    metadata = copy.deepcopy(reader.meta)
    schema_from_file = json.loads(metadata['avro.schema'])
    datum = [data for data in reader]
    reader.close()
    print('avro: {}\n'.format(datum[0]))
    for item in datum:
        print(item)
    original_message = datum[0]['Body']
    print('original message: {}\n'.format(original_message))
    message_bytes = io.BytesIO(original_message)
    message_bytes.seek(5)
    print("bytes: {}".format(message_bytes))
    fastavroschema = fastavro.schema.load_schema("/Users/cv65ry/Projects/good-things/avrodecode/38.avsc")
    #print("schema loaded:{}".format(fastavroschema))
    record = fastavro.schemaless_reader(message_bytes, fastavroschema)
    print(record)