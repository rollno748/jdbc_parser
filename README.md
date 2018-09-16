# jdbc_parser


This is a parser user to extract the SQL queries from JDBC trace, Format Queries, Find duplicates and Finally to obtain Explain plan for the same.

#Usage Notes:

java -jar parser.jar [Source File path] -o [Destination Directory Path]

eg: java -jar parser.jar /var/log/jdbc_trace.log -o /var/output/
