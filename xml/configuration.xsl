<?xml version="1.0" encoding="UTF-8"?>

<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:c="http://www.esei.uvigo.es/dai/hybridserver">

  <xsl:output method="html" indent="yes" encoding="utf-8" />

  <xsl:template match="/">
    <html>
      <head>
        <title>Configuration</title>
      </head>
      <body>
        <div id="container">
          <h1>Configuration</h1>
          <xsl:apply-templates select="c:configuration" />
        </div>
      </body>
    </html>
  </xsl:template>

  <xsl:template match="c:configuration">
    <div class="connections">
      <h2>Connections</h2>
      <div class="http">
        <strong>HTTP: </strong>
        <xsl:value-of select="c:connections/c:http" />
      </div>
      <div class="webservice">
        <strong>Web Service: </strong>
        <xsl:value-of select="c:connections/c:webservice" />
      </div>
      <div class="numClients">
        <strong>NumClients: </strong>
        <xsl:value-of select="c:connections/c:numClients" />
      </div>
    </div>
    <div class="database">
      <h2>Database</h2>
      <div class="user">
        <strong>Usuario: </strong>
        <xsl:value-of select="c:database/c:user" />
      </div>
      <div class="password">
        <strong>Contraseña: </strong>
        <xsl:value-of select="c:database/c:password" />
      </div>
      <div class="url">
        <strong>URL: </strong>
        <xsl:value-of select="c:database/c:url" />
      </div>
    </div>
    <div class="servers">
      <h2>Servers</h2>
      <ul>
        <xsl:for-each select="c:servers/c:server">
          <li>
            <h3>
              <xsl:value-of select="@name" />
            </h3>
            <div class="wsdl">
              <strong>WSDL: </strong>
              <xsl:value-of select="@wsdl" />
            </div>
            <div class="namespace">
              <strong>NameSpace: </strong>
              <xsl:value-of select="@namespace" />
            </div>
            <div class="service">
              <strong>Service:</strong>
              <xsl:value-of select="@service" />
            </div>
            <div class="httpAddress">
              <strong>HTTPAddress: </strong>
              <xsl:value-of select="@httpAddress" />
            </div>
          </li>
        </xsl:for-each>
      </ul>
    </div>
  </xsl:template>
</xsl:stylesheet>
