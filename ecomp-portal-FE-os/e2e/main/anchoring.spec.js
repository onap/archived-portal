describe('New Test 1', function () {
    it('Test', function (done) {

        browser.driver.manage().window().maximize();

        var container = element(by.css('.sidebar'));
        container.all(by.repeater('group in groups')).then(function(groups) {
            var middleEl = Math.floor(groups.length / 2);

            groups[middleEl].click().then(function(){
                expect(groups[middleEl].getAttribute('class')).toContain('active');
                done();
            });

            //var groupButton = groups[middleEl].element(by.binding('group.name'))

            //groups[middleEl].click().then(function(){
            //    expect(groups[middleEl].getAttribute('class')).toContain('active');
            //    done();
            //});



        });
    });
});

