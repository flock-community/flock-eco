tar cvf secrets.tar --directory=.secrets .npmrc settings.xml
travis encrypt-file secrets.tar