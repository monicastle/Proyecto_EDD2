<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
    <xsl:template match="/PersonFile">
        <html>
            <body>
                <h2>Personas</h2>
                <table border="1">
                    <tr bgcolor="lightgreen">
                        <th>Person Id</th>
                        <th>Person Name</th>
                        <th>Person Age</th>
                        <th>City Id</th>
                    </tr>
                    <xsl:for-each select="Registro">
                        <tr>
                            <td>
                                <xsl:value-of select="PersonId"/>
                            </td>
                            <td>
                                <xsl:value-of select="PersonName"/>
                            </td>
                            <td>
                                <xsl:value-of select="PersonAge"/>
                            </td>
                            <td>
                                <xsl:value-of select="CityId"/>
                            </td>
                        </tr>
                    </xsl:for-each>
                </table>
            </body>
        </html>
    </xsl:template>
</xsl:stylesheet>
