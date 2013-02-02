
include siteconfig.mk

TEST_EXECUTABLE=testserver
MAIN_CLASS=de.metux.nanoweb.example.srv
GCJ_ARGS=

JAR_FILE=jnanoweb.jar
LIBRARY_NAME=jnanoweb-cni
LIBRARY_MAJOR=1
LIBRARY_MINOR=0

LIBRARY_FILE=lib$(LIBRARY_NAME).so.$(LIBRARY_MAJOR).$(LIBRARY_MINOR)
LIBRARY_LINK1=lib$(LIBRARY_NAME).so
LIBRARY_LINK2=lib$(LIBRARY_NAME).so.$(LIBRARY_MAJOR)

PKGCONFIG_FILE=$(LIBRARY_NAME).pc

compile:	cni pkgconfig $(TEST_EXECUTABLE)

jar:		$(JAR_FILE)

cni:		$(LIBRARY_FILE)

pkgconfig:	$(PKGCONFIG_FILE)

$(PKGCONFIG_FILE):	$(PKGCONFIG_FILE).in
	@cat $<	\
	    | sed -e 's~@PREFIX@~$(PREFIX)~'			\
	    | sed -e 's~@LIBDIR@~$(LIBDIR)~'			\
	    | sed -e 's~@EXEC_PREFIX@~$(EXEC_PREFIX)~'		\
	    | sed -e 's~@INCLUDEDIR@~$(INCLUDEDIR)~'		\
	    | sed -e 's~@LIBRARY_NAME@~$(LIBRARY_NAME)~'	\
	    | sed -e 's~@LIBRARY_MAJOR@~$(LIBRARY_MAJOR)~'	\
	    | sed -e 's~@LIBRARY_MINOR@~$(LIBRARY_MINOR)~'	\
	    | sed -e 's~@JARDIR@~$(JARDIR)~'			\
	    | sed -e 's~@JAR_FILE@~$(JAR_FILE)~'		\
	    > $@

$(JAR_FILE):
	@gcj $(GCJ_ARGS) -C -d classes `find src -name "*.java"`
	@jar cvf $(JAR_FILE) -C classes .

$(LIBRARY_FILE):	$(JAR_FILE)
	@gcj -shared -fPIC -o $(LIBRARY_FILE) $(JAR_FILE)

$(TEST_EXECUTABLE):	$(JAR_FILE)
	@rm -Rf classes
	@mkdir -p classes
	@javac -d classes `find src -name "*.java"`
	@gcj $(GCJ_ARGS) `find src -name "*.java"` -o $(TEST_EXECUTABLE) --main=$(MAIN_CLASS)

clean:
	@rm -Rf classes $(TEST_EXECUTABLE) tmp $(JAR_FILE) $(LIBRARY_FILE) $(LIBRARY_LINK1) $(LIBRARY_LINK2) $(PKGCONFIG_FILE)

test:	$(TEST_EXECUTABLE)
	./$(TEST_EXECUTABLE)

policy:
	@for i in `find -name "*.java"` ; do \
		astyle --style=java --indent=tab --suffix=none --indent-switches < "$$i" > "$$i.tmp" 2>&1 | grep -ve "^unchanged" ; \
		mv "$$i.tmp" "$$i" ; \
	done

doc:
	@javadoc -d javadoc `find src -name "*.java"`

install:	$(JAR_FILE) $(LIBRARY_FILE) $(PKGCONFIG_FILE)
	@mkdir -p $(DESTDIR)/$(LIBDIR) $(DESTDIR)/$(JARDIR) $(DESTDIR)/$(PKGCONFIGDIR)
	@cp --preserve $(LIBRARY_FILE) $(DESTDIR)/$(LIBDIR)
	@chmod +x $(DESTDIR)/$(LIBDIR)/$(LIBRARY_FILE)
	@cd $(DESTDIR)/$(LIBDIR) && ln -sf $(LIBRARY_FILE) $(LIBRARY_LINK2)
	@cd $(DESTDIR)/$(LIBDIR) && ln -sf $(LIBRARY_LINK2) $(LIBRARY_LINK1)
	@cp --preserve $(JAR_FILE) $(DESTDIR)/$(JARDIR)
	@cp --preserve $(PKGCONFIG_FILE) $(DESTDIR)/$(PKGCONFIGDIR)
