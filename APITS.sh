#! /bin/sh

# There is no need to call this if you set the MULE_HOME in your environment
# but you must also define MULE_LIB for the example (see below)
# or specify the config as a file: URI (see README.txt)
if [ -z "$MULE_HOME" ] ; then
  # find the name of the real MULE_HOME which is two levels up
  MULE_HOME=`pwd`
  MULE_HOME=`dirname "$MULE_HOME"`
  MULE_HOME=`dirname "$MULE_HOME"`
  export MULE_HOME
fi

# If MULE_BASE is not set, make it MULE_HOME
if [ -z "$MULE_BASE" ] ; then
  MULE_BASE="$MULE_HOME"
  export MULE_BASE
fi

#FX stuff, extracted from the Weblogic configuration on KFX2.0 Linux VM
FX_INSTANCE="/home/arborfx"
FX_SITE="${FX_INSTANCE}/site_specific/apits/javaclient/java"
ORDERING_SITE="${FX_SITE}"
SECURITY_SITE="${FX_INSTANCE}/FXSecServer-1.4.0.1/server/config"
ARBOR3P="/opt/arbor3p"

TMNOTHREADS=""
export TMNOTHREADS
LANG=C
export LANG

WSNADDR="//192.168.204.128:10800"
export WSNADDR
# must use 32 bit libs for terrapin and tuxedo
#TERRAPIN_HOME="/vol01/dgtsun01/demo/arborfx2/client/lib32"
# don't think these are 32 bit libs:
#TERRAPIN_HOME="${FX_INSTANCE}/bsdm_site/lib"
TERRAPIN_HOME="${FX_INSTANCE}/site_specific/apits/javaclient/lib"
export TERRAPIN_HOME

# this is old, but 32 bit (32 bit needed)
#TUXDIR="${ARBOR3P}/tuxedo81_119"
TUXDIR="/opt/arbor3p/tuxedo9.1"
export TUXDIR

LD_LIBRARY_PATH="${TERRAPIN_HOME}:${TUXDIR}/lib:${LD_LIBRARY_PATH}"
export LD_LIBRARY_PATH
#FLDTBLDIR="${FX_INSTANCE}/bsdm_site/config:${TUXDIR}/udataobj"
FLDTBLDIR="${FX_INSTANCE}/site_specific/apits/javaclient/config:${TUXDIR}/udataobj"
FIELDTBLS="bali.fml,ShieldWare.fml,tpadm,Usysflds"
#FLDTBLDIR32="${FX_INSTANCE}/bsdm_site/config:${TUXDIR}/udataobj"
FLDTBLDIR32="${FX_INSTANCE}/site_specific/apits/javaclient/config:${TUXDIR}/udataobj"
FIELDTBLS32="bali.fml,ShieldWare.fml,tpadm,Usysflds"
export FLDTBLDIR FIELDTBLS
export FLDTBLDIR32 FIELDTBLS32

FX_JARS="${FX_JARS}:${FX_SITE}/bali.jar"
FX_JARS="${FX_JARS}:${FX_SITE}/aruba.jar"
FX_JARS="${FX_JARS}:${FX_SITE}/shieldware.jar"
FX_JARS="${FX_JARS}:${FX_SITE}/tuxedo.jar"
FX_JARS="${FX_JARS}:${FX_SITE}/FXSecFwkClientASD.jar"
FX_JARS="${FX_JARS}:${FX_SITE}/JNITuxedo.jar"
FX_JARS="${FX_JARS}:${FX_SITE}/commons-logging-1.0.3.jar"
# Mule has it's own log4j
#FX_JARS="${FX_JARS}:${FX_SITE}/log4j-1.2.8.jar"
FX_JARS="${FX_JARS}:${FX_SITE}/jaxb-api.jar"
FX_JARS="${FX_JARS}:${ORDERING_SITE}/ordering.jar"
#FX_JARS="${FX_JARS}:${SECURITY_SITE}/FXSecBPRules.jar"
FX_JARS="${FX_JARS}:${FX_SITE}/FXSecBPRules.jar"

FX_JARS="${FX_JARS}:${FX_SITE}/jaxb-impl.jar"
FX_JARS="${FX_JARS}:${FX_SITE}/jaxb-libs.jar"
FX_JARS="${FX_JARS}:${FX_SITE}/xsdlib.jar"
FX_JARS="${FX_JARS}:${FX_SITE}/shieldware.jar"

#CLASSPATH="${CLASSPATH}:${FX_SITE}:${FX_JARS}:"
#export CLASSPATH

# This extends the classpath to include the configuration directory
# Any changes to the files in ./conf will take precedence over those deployed to $MULE_HOME/lib/user
MULE_LIB=./conf:./bin:$FX_JARS:$FX_SITE
export MULE_LIB

exec "$MULE_BASE/bin/mule" -config apits-config.xml -M-Dlog4j.configuration=file:///opt/APITS/log4j.properties -debug 