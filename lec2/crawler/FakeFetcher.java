import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

interface IFakeFetcher {
    List<String> Fetch(String url);
}

public class FakeFetcher implements IFakeFetcher {
    private static final Map<String, FakeResult> innerFetcher;

    static class FakeResult {
        private List<String> urls;

        void setBody(String body) {
        }

        void setUrls(List<String> urls) {
            this.urls = urls;
        }
    }

    static {
        innerFetcher = new HashMap<>() {
            {
                put("http://golang.org/", new FakeResult() {
                    {
                        setBody("The Go Programming Language");
                        setUrls(new ArrayList<>() {
                            {
                                add("http://golang.org/pkg/");
                                add("http://golang.org/cmd/");
                            }
                        });
                    }
                });
                put("http://golang.org/pkg/", new FakeResult() {
                    {
                        setBody("Packages");
                        setUrls(new ArrayList<>() {
                            {
                                add("http://golang.org/");
                                add("http://golang.org/cmd/");
                                add("http://golang.org/pkg/fmt/");
                                add("http://golang.org/pkg/os/");
                            }
                        });
                    }
                });
                put("http://golang.org/pkg/fmt/", new FakeResult() {
                    {
                        setBody("Package fmt");
                        setUrls(new ArrayList<>() {
                            {
                                add("http://golang.org/");
                                add("http://golang.org/pkg/");
                            }
                        });
                    }
                });
                put("http://golang.org/pkg/os/", new FakeResult() {
                    {
                        setBody("Package os");
                        setUrls(new ArrayList<>() {
                            {
                                add("http://golang.org/");
                                add("http://golang.org/pkg/");
                            }
                        });
                    }
                });
            }
        };
    }

    @Override
    public List<String> Fetch(String url) {
        if (innerFetcher.containsKey(url)) {
            System.out.println("found:   " + url);
            return innerFetcher.get(url).urls;
        }
        System.out.println("missing:   " + url);
        return null;
    }
}
