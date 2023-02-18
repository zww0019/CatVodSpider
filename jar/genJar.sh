rm "./custom_spider.jar"
rm -rf "./Smali_classes"

java -jar "./3rd/baksmali-2.5.2.jar" d "./../app/build/intermediates/dex/release/minifyReleaseWithR8/classes.dex" -o "./Smali_classes"

rm -rf "./spider.jar/smali/com/github/catvod/spider"
rm -rf "./spider.jar/smali/com/github/catvod/parser"

#if not exist "./spider.jar/smali/com/github/catvod/" mkdir "./spider.jar/smali/com/github/catvod/"

if [ ! -d "./spider.jar/smali/com/github/catvod/" ]; then
  mkdir ./spider.jar/smali/com/github/catvod/
fi

#if "%1" == "ec" (
#    java -Dfile.encoding=utf-8 -jar "./3rd/oss.jar" "./Smali_classes"
#)

mv "./Smali_classes/com/github/catvod/spider" "./spider.jar/smali/com/github/catvod/"
mv "./Smali_classes/com/github/catvod/parser" "./spider.jar/smali/com/github/catvod/"

rm -rf "./Smali_classes"

java -jar "./3rd/apktool_2.4.1.jar" b "./spider.jar" -c

mv "./spider.jar/dist/dex.jar" "./custom_spider.jar"

#certUtil -hashfile "./custom_spider.jar" MD5 | find /i /v "md5" | find /i /v "certutil" > "./custom_spider.jar.md5"

rm -rf "./spider.jar/smali/com/github/catvod/spider"
rm -rf "./spider.jar/smali/com/github/catvod/parser"

rm -rf "./spider.jar/build"
rm -rf "./spider.jar/dist"
