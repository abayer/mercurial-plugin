package hudson.plugins.mercurial.browser;

import hudson.Extension;
import hudson.plugins.mercurial.MercurialChangeSet;
import hudson.util.FormValidation;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;


import org.kohsuke.stapler.DataBoundConstructor;
import org.kohsuke.stapler.QueryParameter;

/**
 * Mercurial web interface served using a <a
 * href="http://rhodecode.org/">RhodeCode</a> repository.
 */
public class RhodeCode extends HgBrowser {

    @DataBoundConstructor
    public RhodeCode(String url) throws MalformedURLException {
        super(url);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public URL getChangeSetLink(MercurialChangeSet changeSet)
            throws IOException {
        current = changeSet;
        // http://demo.rhodecode.org/rhodecode/changeset/55a4cbcd464de2f3969ce680885b3193234a8854
        return new URL(getUrl(), "changeset/" + changeSet.getNode());
    }

    /**
     * {@inheritDoc}
     * 
     * Throws {@link IllegalStateException} when this method is called before at
     * least one call to {@literal getChangeSetLink(MercurialChangeSet)}.
     */
    @Override
    public URL getFileLink(String path) throws MalformedURLException {
        checkCurrentIsNotNull();
        // http://demo.rhodecode.org/rhodecode/files/55a4cbcd464de2f3969ce680885b3193234a8854/rhodecode/templates/base/base.html
        return new URL(getUrl(), "files/" + current.getNode() + "/" + path);
    }

    /**
     * {@inheritDoc}
     * 
     * Throws {@link IllegalStateException} when this method is called before at
     * least one call to {@literal getChangeSetLink(MercurialChangeSet)}.
     */
    @Override
    public URL getDiffLink(String path) throws MalformedURLException {
        checkCurrentIsNotNull();
        // http://demo.rhodecode.org/rhodecode/changeset/55a4cbcd464de2f3969ce680885b3193234a8854#Crhodecode-templates-base-base.html
        // this actually points to the getChangeSetLink() page, and just adds an
        // anchor
        //
        // Rhodecode also has a specific page for a single diff, but that
        // requires parameters we don't have here.
        // http://demo.rhodecode.org/rhodecode/diff/rhodecode/templates/admin/users/user_edit_my_account.html?diff2=55a4cbcd464de2f3969ce680885b3193234a8854&diff1=9c0f5d5587895561148b46c68b989d19eeddb0ff&diff=diff
        return new URL(getUrl(), "changeset/" + current.getNode() + "#C"
                + path.replace("/", "-"));
    }

    @Extension
    public static class DescriptorImpl extends HgBrowserDescriptor {
        public String getDisplayName() {
            return "rhodecode";
        }

        @Override public FormValidation doCheckUrl(@QueryParameter String url) {
            return _doCheckUrl(url);
        }

    }

    private static final long serialVersionUID = 1L;
}
