# Problem statement

<p> Implement a web crawler that traverses websites following predefined link depth (8 by default)
and max visited pages limit (10000 by default). Web crawler starts from predefined URL (seed) and
follows links found to dive deeper. The main purpose of this crawler to detect the presence of 
some terms on the page and collect statistics, e.g. </p>

# Seed:

<p>https://en.wikipedia.org/wiki/Elon_Musk</p>

# Configuration

<p>In file application.properties you can point out your own settings such as
- max depth
- max count of urls
- terms
- start page </p>

# Run
<p>mvn clean install</p>
<p>mvn exec:java</p>

