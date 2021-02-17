#!/usr/bin/python3
# Python 3 with `avro-python3` package available
import copy
import simplejson as json
import avro
import io
import fastavro
from avro.datafile import DataFileWriter, DataFileReader
from avro.io import DatumWriter, DatumReader, BinaryDecoder

# Note that we combined namespace and name to get "full name"
# schema = {
#     'name': 'avro.example.User',
#     'type': 'record',
#     'fields': [
#         {'name': 'name', 'type': 'string'},
#         {'name': 'age', 'type': 'int'}
#     ]
# }

# # Parse the schema so we can use it to write the data
# schema_parsed = avro.schema.Parse(json.dumps(schema))

# # Write data to an avro file
# with open('users.avro', 'wb') as f:
#     writer = DataFileWriter(f, DatumWriter(), schema_parsed)
#     writer.append({'name': 'Pierre-Simon Laplace', 'age': 77})
#     writer.append({'name': 'John von Neumann', 'age': 53})
#     writer.close()

def decode(msg_value):
    #print("TYPE: {}".type(msg_value))
    b = str(msg_value, 'utf-8')
    print("VALUE: {}".format(b))
    message_bytes = io.BytesIO(b)
    message_bytes.seek(5)
    decoder = avro.io.BinaryDecoder(message_bytes)
    event_dict = reader.read(decoder)
    return event_dict

# Read data from an avro file
with open('/home/allan/good-things/avrodecode/38.avro', 'rb') as f:
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
    # attempt 1
    # schema = avro.schema.Parse(open("/home/allan/good-things/avrodecode/38.avsc").read())
    # reader = DatumReader(schema)
    # print('decoded message: {}\n'.format(decode(original_message)))
    
    # attempt 2
    message_bytes = io.BytesIO(original_message)
    message_bytes.seek(5)
    print("bytes: {}".format(message_bytes))
    fastavroschema = fastavro.schema.load_schema("/home/allan/good-things/avrodecode/38.avsc")
    #print("schema loaded:{}".format(fastavroschema))
    record = fastavro.schemaless_reader(message_bytes, fastavroschema)
    print(record)

# print('Schema that we specified {}:\n'.format(schema))
# print('Schema that we parsed: {}\n'.format(schema_parsed))
# print('Schema from users.avro file: {}\n'.format(schema_from_file))
# print('avro: {}\n'.format(datum))

# Schema that we specified:
#  {'name': 'avro.example.User', 'type': 'record',
#   'fields': [{'name': 'name', 'type': 'string'}, {'name': 'age', 'type': 'int'}]}
# Schema that we parsed:
#  {"type": "record", "name": "User", "namespace": "avro.example",
#   "fields": [{"type": "string", "name": "name"}, {"type": "int", "name": "age"}]}
# Schema from users.avro file:
#  {'type': 'record', 'name': 'User', 'namespace': 'avro.example',
#   'fields': [{'type': 'string', 'name': 'name'}, {'type': 'int', 'name': 'age'}]}
# Users:
#  [{'name': 'Pierre-Simon Laplace', 'age': 77}, {'name': 'John von Neumann', 'age': 53}]
