var acid = new Array();
var aname = new Array();
var apid = new Array();
var pid= new Array();
var pname= new Array();

//增加了对省会的处理
//选择地区后，自动选择该地区活省份的省会
var pBigCity = new Array() ; //add by yangzi

/*
   pid[0]=0;
   pname[0]='请选择';
pBigCity[0]=0;

   acid[0]=0;
   aname[0]='请选择';
   apid[0]=0;
*/

//---------------------------
   pid[1]=85210;
   pname[1]='香港';
pBigCity[1] = 8521010;

   acid[1]=8521010;
   aname[1]='香港';
   apid[1]=85210;

   pid[2]=85310;
   pname[2]='澳门';
pBigCity[2] = 8531010 ;

   acid[2]=8531010;
   aname[2]='澳门';
   apid[2]=85310;
pBigCity[3] = 861010 ;

   pid[3]=8610;
   pname[3]='北京';

   acid[3]=861010;
   aname[3]='北京';
   apid[3]=8610;

   pid[4]=8620;
   pname[4]='广东';
pBigCity[4]=862020;

   acid[4]=862020;
   aname[4]='广州';
   apid[4]=8620;

   acid[5]=8620660;
   aname[5]='汕尾';
   apid[5]=8620;

   acid[6]=8620661;
   aname[6]='潮阳';
   apid[6]=8620;

   acid[7]=8620662;
   aname[7]='阳江';
   apid[7]=8620;

   acid[8]=8620663;
   aname[8]='揭阳';
   apid[8]=8620;

   acid[9]=8620668;
   aname[9]='茂名';
   apid[9]=8620;

   acid[10]=8620750;
   aname[10]='江门';
   apid[10]=8620;

   acid[11]=8620751;
   aname[11]='韶关';
   apid[11]=8620;

   acid[12]=8620752;
   aname[12]='惠州';
   apid[12]=8620;

   acid[13]=8620753;
   aname[13]='梅州';
   apid[13]=8620;

   acid[14]=8620754;
   aname[14]='汕头';
   apid[14]=8620;

   acid[15]=8620755;
   aname[15]='深圳';
   apid[15]=8620;

   acid[16]=8620756;
   aname[16]='珠海';
   apid[16]=8620;

   acid[17]=8620757;
   aname[17]='佛山';
   apid[17]=8620;

   acid[18]=8620758;
   aname[18]='肇庆';
   apid[18]=8620;

   acid[19]=8620759;
   aname[19]='湛江';
   apid[19]=8620;

   acid[20]=8620760;
   aname[20]='中山';
   apid[20]=8620;

   acid[21]=8620762;
   aname[21]='河源';
   apid[21]=8620;

   acid[22]=8620763;
   aname[22]='清远';
   apid[22]=8620;

   acid[23]=8620765;
   aname[23]='顺德';
   apid[23]=8620;

   acid[24]=8620766;
   aname[24]='云浮';
   apid[24]=8620;

   acid[25]=8620768;
   aname[25]='潮州';
   apid[25]=8620;

   acid[26]=8620769;
   aname[26]='东莞';
   apid[26]=8620;

   pid[5]=8621;
   pname[5]='上海';
pBigCity[5]=862121;

   acid[27]=862121;
   aname[27]='上海';
   apid[27]=8621;

   pid[6]=8622;
   pname[6]='天津';
pBigCity[6]=862222;

   acid[28]=862222;
   aname[28]='天津';
   apid[28]=8622;

   pid[7]=8623;
   pname[7]='重庆';
pBigCity[7]=862323;

   acid[29]=862323;
   aname[29]='重庆';
   apid[29]=8623;

   acid[30]=8623810;
   aname[30]='涪陵';
   apid[30]=8623;

   acid[31]=8623819;
   aname[31]='万县';
   apid[31]=8623;

   pid[8]=8624;
   pname[8]='辽宁';
pBigCity[8]=862424;

   acid[32]=862424;
   aname[32]='沈阳';
   apid[32]=8624;

   acid[33]=8624410;
   aname[33]='铁岭';
   apid[33]=8624;

   acid[34]=8624411;
   aname[34]='大连';
   apid[34]=8624;

   acid[35]=8624412;
   aname[35]='鞍山';
   apid[35]=8624;

   acid[36]=8624413;
   aname[36]='抚顺';
   apid[36]=8624;

   acid[37]=8624414;
   aname[37]='本溪';
   apid[37]=8624;

   acid[38]=8624415;
   aname[38]='丹东';
   apid[38]=8624;

   acid[39]=8624416;
   aname[39]='锦州';
   apid[39]=8624;

   acid[40]=8624417;
   aname[40]='营口';
   apid[40]=8624;

   acid[41]=8624418;
   aname[41]='阜新';
   apid[41]=8624;

   acid[42]=8624419;
   aname[42]='辽阳';
   apid[42]=8624;

   acid[43]=8624421;
   aname[43]='朝阳';
   apid[43]=8624;

   acid[44]=8624427;
   aname[44]='盘锦';
   apid[44]=8624;

   acid[45]=8624429;
   aname[45]='葫芦岛';
   apid[45]=8624;

   pid[9]=8625;
   pname[9]='江苏';
pBigCity[9]=862525;

   acid[46]=862525;
   aname[46]='南京';
   apid[46]=8625;

   acid[47]=8625510;
   aname[47]='无锡';
   apid[47]=8625;

   acid[48]=8625511;
   aname[48]='镇江';
   apid[48]=8625;

   acid[49]=8625512;
   aname[49]='苏州';
   apid[49]=8625;

   acid[50]=8625513;
   aname[50]='南通';
   apid[50]=8625;

   acid[51]=8625514;
   aname[51]='扬州';
   apid[51]=8625;

   acid[52]=8625515;
   aname[52]='盐城';
   apid[52]=8625;

   acid[53]=8625516;
   aname[53]='徐州';
   apid[53]=8625;

   acid[54]=8625517;
   aname[54]='淮阴';
   apid[54]=8625;

   acid[55]=8625518;
   aname[55]='连云港';
   apid[55]=8625;

   acid[56]=8625519;
   aname[56]='常州';
   apid[56]=8625;

   acid[57]=8625520;
   aname[57]='常熟';
   apid[57]=8625;

   acid[58]=8625523;
   aname[58]='泰州';
   apid[58]=8625;

   acid[59]=8625527;
   aname[59]='宿迁';
   apid[59]=8625;

   pid[10]=8627;
   pname[10]='湖北';
pBigCity[10]=862727;

   acid[60]=862727;
   aname[60]='武汉';
   apid[60]=8627;

   acid[61]=8627710;
   aname[61]='襄樊';
   apid[61]=8627;

   acid[62]=8627711;
   aname[62]='鄂州';
   apid[62]=8627;

   acid[63]=8627712;
   aname[63]='孝感';
   apid[63]=8627;

   acid[64]=8627713;
   aname[64]='黄冈';
   apid[64]=8627;

   acid[65]=8627714;
   aname[65]='黄石';
   apid[65]=8627;

   acid[66]=8627715;
   aname[66]='咸宁';
   apid[66]=8627;

   acid[67]=8627716;
   aname[67]='荆州';
   apid[67]=8627;

   acid[68]=8627717;
   aname[68]='宜昌';
   apid[68]=8627;

   acid[69]=8627718;
   aname[69]='恩施';
   apid[69]=8627;

   acid[70]=8627719;
   aname[70]='十堰';
   apid[70]=8627;

   acid[71]=8627722;
   aname[71]='随州';
   apid[71]=8627;

   acid[72]=8627724;
   aname[72]='荆门';
   apid[72]=8627;

   acid[73]=8627728;
   aname[73]='江汉';
   apid[73]=8627;

   pid[11]=8628;
   pname[11]='四川';
pBigCity[11]=862828;

   acid[74]=862828;
   aname[74]='成都';
   apid[74]=8628;

   acid[75]=8628811;
   aname[75]='重庆';
   apid[75]=8628;

   acid[76]=8628812;
   aname[76]='攀枝花';
   apid[76]=8628;

   acid[77]=8628813;
   aname[77]='自贡';
   apid[77]=8628;

   acid[78]=8628816;
   aname[78]='绵阳';
   apid[78]=8628;

   acid[79]=8628817;
   aname[79]='南充';
   apid[79]=8628;

   acid[80]=8628818;
   aname[80]='达川';
   apid[80]=8628;

   acid[81]=8628825;
   aname[81]='遂宁';
   apid[81]=8628;

   acid[82]=8628826;
   aname[82]='广安';
   apid[82]=8628;

   acid[83]=8628827;
   aname[83]='巴中';
   apid[83]=8628;

   acid[84]=8628830;
   aname[84]='泸州';
   apid[84]=8628;

   acid[85]=8628831;
   aname[85]='宜宾';
   apid[85]=8628;

   acid[86]=8628832;
   aname[86]='资阳';
   apid[86]=8628;

   acid[87]=8628833;
   aname[87]='乐山、眉山';
   apid[87]=8628;

   acid[88]=8628834;
   aname[88]='西昌';
   apid[88]=8628;

   acid[89]=8628835;
   aname[89]='雅安';
   apid[89]=8628;

   acid[90]=8628836;
   aname[90]='康定';
   apid[90]=8628;

   acid[91]=8628837;
   aname[91]='马尔康';
   apid[91]=8628;

   acid[92]=8628838;
   aname[92]='德阳';
   apid[92]=8628;

   acid[93]=8628839;
   aname[93]='广元';
   apid[93]=8628;

   pid[12]=8629;
   pname[12]='陕西';
pBigCity[12]=862929;

   acid[94]=862929;
   aname[94]='西安';
   apid[94]=8629;

   acid[95]=8629910;
   aname[95]='咸阳';
   apid[95]=8629;

   acid[96]=8629911;
   aname[96]='延安';
   apid[96]=8629;

   acid[97]=8629912;
   aname[97]='榆林';
   apid[97]=8629;

   acid[98]=8629913;
   aname[98]='渭南';
   apid[98]=8629;

   acid[99]=8629914;
   aname[99]='商洛';
   apid[99]=8629;

   acid[100]=8629915;
   aname[100]='安康';
   apid[100]=8629;

   acid[101]=8629916;
   aname[101]='汉中';
   apid[101]=8629;

   acid[102]=8629917;
   aname[102]='宝鸡';
   apid[102]=8629;

   acid[103]=8629919;
   aname[103]='铜川';
   apid[103]=8629;

   pid[13]=86311;
   pname[13]='河北';
pBigCity[13]=86311311;

   acid[104]=86311310;
   aname[104]='邯郸';
   apid[104]=86311;

   acid[105]=86311311;
   aname[105]='石家庄';
   apid[105]=86311;

   acid[106]=86311312;
   aname[106]='保定';
   apid[106]=86311;

   acid[107]=86311313;
   aname[107]='张家口';
   apid[107]=86311;

   acid[108]=86311314;
   aname[108]='承德';
   apid[108]=86311;

   acid[109]=86311315;
   aname[109]='唐山';
   apid[109]=86311;

   acid[110]=86311316;
   aname[110]='廊坊';
   apid[110]=86311;

   acid[111]=86311317;
   aname[111]='沧州';
   apid[111]=86311;

   acid[112]=86311318;
   aname[112]='衡水';
   apid[112]=86311;

   acid[113]=86311319;
   aname[113]='邢台';
   apid[113]=86311;

   acid[114]=86311335;
   aname[114]='秦皇岛';
   apid[114]=86311;

   pid[14]=86351;
   pname[14]='山西';
pBigCity[14]=86351351;

   acid[115]=86351349;
   aname[115]='朔州';
   apid[115]=86351;

   acid[116]=86351350;
   aname[116]='忻州';
   apid[116]=86351;

   acid[117]=86351351;
   aname[117]='太原';
   apid[117]=86351;

   acid[118]=86351352;
   aname[118]='大同';
   apid[118]=86351;

   acid[119]=86351353;
   aname[119]='阳泉';
   apid[119]=86351;

   acid[120]=86351354;
   aname[120]='晋中';
   apid[120]=86351;

   acid[121]=86351355;
   aname[121]='长治';
   apid[121]=86351;

   acid[122]=86351356;
   aname[122]='晋城';
   apid[122]=86351;

   acid[123]=86351357;
   aname[123]='临汾';
   apid[123]=86351;

   acid[124]=86351358;
   aname[124]='吕梁';
   apid[124]=86351;

   acid[125]=86351359;
   aname[125]='运城';
   apid[125]=86351;

   pid[15]=86371;
   pname[15]='河南';
pBigCity[15]=86371371;

   acid[126]=86371370;
   aname[126]='商丘';
   apid[126]=86371;

   acid[127]=86371371;
   aname[127]='郑州';
   apid[127]=86371;

   acid[128]=86371372;
   aname[128]='安阳';
   apid[128]=86371;

   acid[129]=86371373;
   aname[129]='新乡';
   apid[129]=86371;

   acid[130]=86371374;
   aname[130]='许昌';
   apid[130]=86371;

   acid[131]=86371375;
   aname[131]='平顶山';
   apid[131]=86371;

   acid[132]=86371376;
   aname[132]='信阳';
   apid[132]=86371;

   acid[133]=86371377;
   aname[133]='南阳';
   apid[133]=86371;

   acid[134]=86371378;
   aname[134]='开封';
   apid[134]=86371;

   acid[135]=86371379;
   aname[135]='洛阳';
   apid[135]=86371;

   acid[136]=86371391;
   aname[136]='焦作';
   apid[136]=86371;

   acid[137]=86371392;
   aname[137]='鹤壁';
   apid[137]=86371;

   acid[138]=86371393;
   aname[138]='濮阳';
   apid[138]=86371;

   acid[139]=86371394;
   aname[139]='周口';
   apid[139]=86371;

   acid[140]=86371395;
   aname[140]='漯河';
   apid[140]=86371;

   acid[141]=86371396;
   aname[141]='驻马店';
   apid[141]=86371;

   acid[142]=86371397;
   aname[142]='潢川';
   apid[142]=86371;

   acid[143]=86371398;
   aname[143]='三门峡';
   apid[143]=86371;

   pid[16]=86431;
   pname[16]='吉林';
pBigCity[16]=86431431;

   acid[144]=86431431;
   aname[144]='长春';
   apid[144]=86431;

   acid[145]=86431432;
   aname[145]='吉林';
   apid[145]=86431;

   acid[146]=86431433;
   aname[146]='延吉';
   apid[146]=86431;

   acid[147]=86431434;
   aname[147]='四平';
   apid[147]=86431;

   acid[148]=86431435;
   aname[148]='通化';
   apid[148]=86431;

   acid[149]=86431436;
   aname[149]='白城';
   apid[149]=86431;

   acid[150]=86431437;
   aname[150]='辽源';
   apid[150]=86431;

   acid[151]=86431438;
   aname[151]='松原';
   apid[151]=86431;

   acid[152]=86431439;
   aname[152]='白山';
   apid[152]=86431;

   acid[153]=86431440;
   aname[153]='珲春';
   apid[153]=86431;

   acid[154]=86431448;
   aname[154]='梅河';
   apid[154]=86431;

   pid[17]=86451;
   pname[17]='黑龙江';
pBigCity[17]=86451451;

   acid[155]=86451450;
   aname[155]='阿城';
   apid[155]=86451;

   acid[156]=86451451;
   aname[156]='哈尔滨';
   apid[156]=86451;

   acid[157]=86451452;
   aname[157]='齐齐哈尔';
   apid[157]=86451;

   acid[158]=86451453;
   aname[158]='牡丹江';
   apid[158]=86451;

   acid[159]=86451454;
   aname[159]='佳木斯';
   apid[159]=86451;

   acid[160]=86451455;
   aname[160]='绥化';
   apid[160]=86451;

   acid[161]=86451456;
   aname[161]='黑河';
   apid[161]=86451;

   acid[162]=86451458;
   aname[162]='伊春';
   apid[162]=86451;

   acid[163]=86451459;
   aname[163]='大庆';
   apid[163]=86451;

   pid[18]=86471;
   pname[18]='内蒙古';
pBigCity[18]=86471471;

   acid[164]=86471470;
   aname[164]='海拉尔';
   apid[164]=86471;

   acid[165]=86471471;
   aname[165]='呼和浩特';
   apid[165]=86471;

   acid[166]=86471472;
   aname[166]='包头';
   apid[166]=86471;

   acid[167]=86471473;
   aname[167]='乌海';
   apid[167]=86471;

   acid[168]=86471474;
   aname[168]='集宁';
   apid[168]=86471;

   acid[169]=86471475;
   aname[169]='通辽';
   apid[169]=86471;

   acid[170]=86471476;
   aname[170]='赤峰';
   apid[170]=86471;

   acid[171]=86471477;
   aname[171]='东胜';
   apid[171]=86471;

   acid[172]=86471478;
   aname[172]='临河';
   apid[172]=86471;

   acid[173]=86471479;
   aname[173]='锡林浩特';
   apid[173]=86471;

   acid[174]=86471482;
   aname[174]='乌兰浩特';
   apid[174]=86471;

   acid[175]=86471483;
   aname[175]='巴彦浩特';
   apid[175]=86471;

   pid[19]=86531;
   pname[19]='山东';
pBigCity[19]=86531531;

   acid[176]=86531530;
   aname[176]='菏泽';
   apid[176]=86531;

   acid[177]=86531531;
   aname[177]='济南';
   apid[177]=86531;

   acid[178]=86531532;
   aname[178]='青岛';
   apid[178]=86531;

   acid[179]=86531533;
   aname[179]='淄博';
   apid[179]=86531;

   acid[180]=86531534;
   aname[180]='德州';
   apid[180]=86531;

   acid[181]=86531535;
   aname[181]='烟台';
   apid[181]=86531;

   acid[182]=86531536;
   aname[182]='潍坊';
   apid[182]=86531;

   acid[183]=86531537;
   aname[183]='济宁';
   apid[183]=86531;

   acid[184]=86531538;
   aname[184]='泰安';
   apid[184]=86531;

   acid[185]=86531539;
   aname[185]='临沂';
   apid[185]=86531;

   acid[186]=86531543;
   aname[186]='滨州';
   apid[186]=86531;

   acid[187]=86531546;
   aname[187]='东营';
   apid[187]=86531;

   acid[188]=86531631;
   aname[188]='威海';
   apid[188]=86531;

   acid[189]=86531632;
   aname[189]='枣庄';
   apid[189]=86531;

   acid[190]=86531633;
   aname[190]='日照';
   apid[190]=86531;

   acid[191]=86531634;
   aname[191]='莱芜';
   apid[191]=86531;

   acid[192]=86531635;
   aname[192]='聊城';
   apid[192]=86531;

   pid[20]=86551;
   pname[20]='安徽';
pBigCity[20]=86551551;

   acid[193]=86551550;
   aname[193]='滁州';
   apid[193]=86551;

   acid[194]=86551551;
   aname[194]='合肥';
   apid[194]=86551;

   acid[195]=86551552;
   aname[195]='蚌埠';
   apid[195]=86551;

   acid[196]=86551553;
   aname[196]='芜湖';
   apid[196]=86551;

   acid[197]=86551554;
   aname[197]='淮南';
   apid[197]=86551;

   acid[198]=86551555;
   aname[198]='马鞍山';
   apid[198]=86551;

   acid[199]=86551556;
   aname[199]='安庆';
   apid[199]=86551;

   acid[200]=86551557;
   aname[200]='宿县';
   apid[200]=86551;

   acid[201]=86551558;
   aname[201]='阜阳';
   apid[201]=86551;

   acid[202]=86551559;
   aname[202]='黄山';
   apid[202]=86551;

   acid[203]=86551561;
   aname[203]='淮北';
   apid[203]=86551;

   acid[204]=86551562;
   aname[204]='铜陵';
   apid[204]=86551;

   acid[205]=86551563;
   aname[205]='宣城';
   apid[205]=86551;

   acid[206]=86551564;
   aname[206]='六安';
   apid[206]=86551;

   acid[207]=86551565;
   aname[207]='巢湖';
   apid[207]=86551;

   acid[208]=86551566;
   aname[208]='池州';
   apid[208]=86551;

   pid[21]=86571;
   pname[21]='浙江';
pBigCity[21]=86571571;

   acid[209]=86571570;
   aname[209]='衢州';
   apid[209]=86571;

   acid[210]=86571571;
   aname[210]='杭州';
   apid[210]=86571;

   acid[211]=86571572;
   aname[211]='湖州';
   apid[211]=86571;

   acid[212]=86571573;
   aname[212]='嘉兴';
   apid[212]=86571;

   acid[213]=86571574;
   aname[213]='宁波';
   apid[213]=86571;

   acid[214]=86571575;
   aname[214]='绍兴';
   apid[214]=86571;

   acid[215]=86571576;
   aname[215]='台州';
   apid[215]=86571;

   acid[216]=86571577;
   aname[216]='温州';
   apid[216]=86571;

   acid[217]=86571578;
   aname[217]='丽水';
   apid[217]=86571;

   acid[218]=86571579;
   aname[218]='金华';
   apid[218]=86571;

   acid[219]=86571580;
   aname[219]='舟山';
   apid[219]=86571;

   pid[22]=86591;
   pname[22]='福建';
pBigCity[22]=86591591;

   acid[220]=86591591;
   aname[220]='福州';
   apid[220]=86591;

   acid[221]=86591592;
   aname[221]='厦门';
   apid[221]=86591;

   acid[222]=86591593;
   aname[222]='宁德';
   apid[222]=86591;

   acid[223]=86591594;
   aname[223]='莆田';
   apid[223]=86591;

   acid[224]=86591595;
   aname[224]='泉州';
   apid[224]=86591;

   acid[225]=86591596;
   aname[225]='漳州';
   apid[225]=86591;

   acid[226]=86591597;
   aname[226]='龙岩';
   apid[226]=86591;

   acid[227]=86591598;
   aname[227]='三明';
   apid[227]=86591;

   acid[228]=86591599;
   aname[228]='南平';
   apid[228]=86591;

   pid[23]=86731;
   pname[23]='湖南';
pBigCity[23]=86731731;

   acid[229]=86731730;
   aname[229]='岳阳';
   apid[229]=86731;

   acid[230]=86731731;
   aname[230]='长沙';
   apid[230]=86731;

   acid[231]=86731732;
   aname[231]='湘潭';
   apid[231]=86731;

   acid[232]=86731733;
   aname[232]='株州';
   apid[232]=86731;

   acid[233]=86731734;
   aname[233]='衡阳';
   apid[233]=86731;

   acid[234]=86731735;
   aname[234]='郴州';
   apid[234]=86731;

   acid[235]=86731736;
   aname[235]='常德';
   apid[235]=86731;

   acid[236]=86731737;
   aname[236]='益阳';
   apid[236]=86731;

   acid[237]=86731738;
   aname[237]='娄底';
   apid[237]=86731;

   acid[238]=86731739;
   aname[238]='邵阳';
   apid[238]=86731;

   acid[239]=86731743;
   aname[239]='吉首';
   apid[239]=86731;

   acid[240]=86731744;
   aname[240]='张家界';
   apid[240]=86731;

   acid[241]=86731745;
   aname[241]='怀化';
   apid[241]=86731;

   acid[242]=86731746;
   aname[242]='永州';
   apid[242]=86731;

   pid[24]=86771;
   pname[24]='广西';
pBigCity[24]=86771771;

   acid[243]=86771770;
   aname[243]='防城港';
   apid[243]=86771;

   acid[244]=86771771;
   aname[244]='南宁';
   apid[244]=86771;

   acid[245]=86771772;
   aname[245]='柳州';
   apid[245]=86771;

   acid[246]=86771773;
   aname[246]='桂林';
   apid[246]=86771;

   acid[247]=86771774;
   aname[247]='梧州';
   apid[247]=86771;

   acid[248]=86771775;
   aname[248]='玉林';
   apid[248]=86771;

   acid[249]=86771776;
   aname[249]='百色';
   apid[249]=86771;

   acid[250]=86771777;
   aname[250]='钦州';
   apid[250]=86771;

   acid[251]=86771778;
   aname[251]='河池';
   apid[251]=86771;

   acid[252]=86771779;
   aname[252]='北海';
   apid[252]=86771;

   pid[25]=86791;
   pname[25]='江西';
pBigCity[25]=86791791;

   acid[253]=86791701;
   aname[253]='鹰潭';
   apid[253]=86791;

   acid[254]=86791790;
   aname[254]='新余';
   apid[254]=86791;

   acid[255]=86791791;
   aname[255]='南昌';
   apid[255]=86791;

   acid[256]=86791792;
   aname[256]='九江';
   apid[256]=86791;

   acid[257]=86791793;
   aname[257]='上饶';
   apid[257]=86791;

   acid[258]=86791794;
   aname[258]='抚州';
   apid[258]=86791;

   acid[259]=86791795;
   aname[259]='宜春';
   apid[259]=86791;

   acid[260]=86791796;
   aname[260]='吉安';
   apid[260]=86791;

   acid[261]=86791797;
   aname[261]='赣州';
   apid[261]=86791;

   acid[262]=86791798;
   aname[262]='景德镇';
   apid[262]=86791;

   acid[263]=86791799;
   aname[263]='萍乡';
   apid[263]=86791;

   pid[26]=86851;
   pname[26]='贵州';
pBigCity[26]=86851851;

   acid[264]=86851851;
   aname[264]='贵阳';
   apid[264]=86851;

   acid[265]=86851852;
   aname[265]='遵义';
   apid[265]=86851;

   acid[266]=86851853;
   aname[266]='安顺';
   apid[266]=86851;

   acid[267]=86851854;
   aname[267]='黔南';
   apid[267]=86851;

   acid[268]=86851855;
   aname[268]='黔东南';
   apid[268]=86851;

   acid[269]=86851856;
   aname[269]='铜仁';
   apid[269]=86851;

   acid[270]=86851857;
   aname[270]='毕节';
   apid[270]=86851;

   acid[271]=86851858;
   aname[271]='六盘水';
   apid[271]=86851;

   acid[272]=86851859;
   aname[272]='黔西南';
   apid[272]=86851;

   pid[27]=86871;
   pname[27]='云南';
pBigCity[27]=86871871;

   acid[273]=86871691;
   aname[273]='西双版纳';
   apid[273]=86871;

   acid[274]=86871692;
   aname[274]='德宏';
   apid[274]=86871;

   acid[275]=86871870;
   aname[275]='昭通';
   apid[275]=86871;

   acid[276]=86871871;
   aname[276]='昆明';
   apid[276]=86871;

   acid[277]=86871872;
   aname[277]='大理';
   apid[277]=86871;

   acid[278]=86871873;
   aname[278]='个旧';
   apid[278]=86871;

   acid[279]=86871874;
   aname[279]='曲靖';
   apid[279]=86871;

   acid[280]=86871875;
   aname[280]='保山';
   apid[280]=86871;

   acid[281]=86871876;
   aname[281]='文山';
   apid[281]=86871;

   acid[282]=86871877;
   aname[282]='玉溪';
   apid[282]=86871;

   acid[283]=86871878;
   aname[283]='楚雄';
   apid[283]=86871;

   acid[284]=86871879;
   aname[284]='思茅';
   apid[284]=86871;

   acid[285]=86871881;
   aname[285]='东川';
   apid[285]=86871;

   acid[286]=86871883;
   aname[286]='临沧';
   apid[286]=86871;

   acid[287]=86871886;
   aname[287]='六库';
   apid[287]=86871;

   acid[288]=86871887;
   aname[288]='中甸';
   apid[288]=86871;

   acid[289]=86871888;
   aname[289]='丽江';
   apid[289]=86871;

   pid[28]=86891;
   pname[28]='西藏';
pBigCity[28]=86891891;

   acid[290]=86891891;
   aname[290]='拉萨';
   apid[290]=86891;

   acid[291]=86891892;
   aname[291]='日喀则';
   apid[291]=86891;

   acid[292]=86891893;
   aname[292]='山南';
   apid[292]=86891;

   acid[293]=86891894;
   aname[293]='林芝';
   apid[293]=86891;

   acid[294]=86891895;
   aname[294]='昌都';
   apid[294]=86891;

   acid[295]=86891896;
   aname[295]='那曲';
   apid[295]=86891;

   acid[296]=86891897;
   aname[296]='阿里';
   apid[296]=86891;

//－－－－－－－－－－－－－
   pid[29]=86898;
   pname[29]='海南';
pBigCity[29]=86898898;


   acid[297]=86898890;
   aname[297]='儋州';
   apid[297]=86898;

   acid[298]=86898898;
   aname[298]='海口';
   apid[298]=86898;

   acid[299]=86898899;
   aname[299]='三亚';
   apid[299]=86898;

   pid[30]=86931;
   pname[30]='甘肃';
pBigCity[30]=86931931;


   acid[300]=86931930;
   aname[300]='临夏';
   apid[300]=86931;

   acid[301]=86931931;
   aname[301]='兰州';
   apid[301]=86931;

   acid[302]=86931932;
   aname[302]='定西';
   apid[302]=86931;

   acid[303]=86931933;
   aname[303]='平凉';
   apid[303]=86931;

   acid[304]=86931934;
   aname[304]='庆阳';
   apid[304]=86931;

   acid[305]=86931935;
   aname[305]='金昌武威';
   apid[305]=86931;

   acid[306]=86931936;
   aname[306]='张掖';
   apid[306]=86931;

   acid[307]=86931937;
   aname[307]='酒泉嘉峪关';
   apid[307]=86931;

   acid[308]=86931938;
   aname[308]='天水';
   apid[308]=86931;

   acid[309]=86931939;
   aname[309]='陇南';
   apid[309]=86931;

   acid[310]=86931943;
   aname[310]='白银';
   apid[310]=86931;

   pid[31]=86951;
   pname[31]='宁夏';
pBigCity[31]=86951951;

   acid[311]=86951951;
   aname[311]='银川';
   apid[311]=86951;

   acid[312]=86951952;
   aname[312]='石嘴山';
   apid[312]=86951;

   acid[313]=86951953;
   aname[313]='吴忠';
   apid[313]=86951;

   acid[314]=86951954;
   aname[314]='固原';
   apid[314]=86951;

   pid[32]=86971;
   pname[32]='青海';
pBigCity[32]=86971971;

   acid[315]=86971970;
   aname[315]='海晏';
   apid[315]=86971;

   acid[316]=86971971;
   aname[316]='西宁';
   apid[316]=86971;

   acid[317]=86971972;
   aname[317]='海东';
   apid[317]=86971;

   acid[318]=86971973;
   aname[318]='同仁';
   apid[318]=86971;

   acid[319]=86971974;
   aname[319]='共和';
   apid[319]=86971;

   acid[320]=86971975;
   aname[320]='玛沁';
   apid[320]=86971;

   acid[321]=86971976;
   aname[321]='玉树';
   apid[321]=86971;

   acid[322]=86971977;
   aname[322]='德令哈';
   apid[322]=86971;

   acid[323]=86971978;
   aname[323]='门源';
   apid[323]=86971;

   acid[324]=86971979;
   aname[324]='格尔木';
   apid[324]=86971;

   pid[33]=86991;
   pname[33]='新疆';
pBigCity[33]=86991991;

   acid[325]=86991901;
   aname[325]='塔城';
   apid[325]=86991;

   acid[326]=86991902;
   aname[326]='哈密';
   apid[326]=86991;

   acid[327]=86991903;
   aname[327]='和田';
   apid[327]=86991;

   acid[328]=86991906;
   aname[328]='阿勒泰';
   apid[328]=86991;

   acid[329]=86991908;
   aname[329]='阿图什';
   apid[329]=86991;

   acid[330]=86991909;
   aname[330]='博乐';
   apid[330]=86991;

   acid[331]=86991990;
   aname[331]='克拉玛依';
   apid[331]=86991;

   acid[332]=86991991;
   aname[332]='乌鲁木齐';
   apid[332]=86991;

   acid[333]=86991992;
   aname[333]='奎屯';
   apid[333]=86991;

   acid[334]=86991993;
   aname[334]='石河子';
   apid[334]=86991;

   acid[335]=86991994;
   aname[335]='昌吉';
   apid[335]=86991;

   acid[336]=86991995;
   aname[336]='吐鲁番';
   apid[336]=86991;

   acid[337]=86991996;
   aname[337]='库尔勒';
   apid[337]=86991;

   acid[338]=86991997;
   aname[338]='阿克苏';
   apid[338]=86991;

   acid[339]=86991998;
   aname[339]='喀什';
   apid[339]=86991;

   acid[340]=86991999;
   aname[340]='伊犁';
   apid[340]=86991;

   pid[34]=88610;
   pname[34]='台湾';
pBigCity[34]=8861010;

   acid[341]=8861010;
   aname[341]='台湾';
   apid[341]=88610;

function showprovince(listobj,selectedprovince)
{
	//alert(listobj+selectedprovince);
	//for(var i=3;i<pid.length;i++) // 此句被yangzi 用下面这以行代替
	for(var i=1;i<pid.length;i++)
	{
		var opt = new Option(pname[i],pid[i]) ;
		if(listobj!=null){
			if(selectedprovince!='undefined'&&selectedprovince==pid[i])//数据回填	
				opt.selected=true;
			listobj.options[listobj.options.length] = opt ;
		}
		opt = null ;
		
	}
/*
	//被yangzi注释掉
	for(var i=0;i<3;i++)
	{

			var opt = new Option(pname[i],pid[i]) ;
			listobj.options[listobj.options.length] = opt ;
			opt = null ;
	}

*/

}

//--------------------------------------------

function showCity(ppid,listobj,selectedcity)
{
	if(ppid==0)return ; //add by yangzi  2006-10-24
	
	//给列表加上"-选择-"的表头 
	if(ppid > 0)
	{
			var opt = new Option("-选择-",0) ;
			listobj.options[listobj.options.length] = opt ;
			opt = null ;
	}
	
	//选出该省的省会数组下标 . add by yangzi  2006-10-24
	var pindex = 0 ;
	for(var j=1;j<pid.length;j++)
	{
		if(pid[j]==ppid)
		{
			pindex = j ;
			break; 
		} 
	}
	
	//选出该省省会的城市编号.add by yangzi  2006-10-24
	var bigCityNum = 0 ;
	for(var m=1;m<pBigCity.length;m++)
	{
		if(m==pindex)	
		{
			bigCityNum =pBigCity[m] ;
			break ;
		}
	}
	
	
	//var bigCityIndex = 0 ; //用来保存大城市的索引
	var cityIndex = 0 ;    //记录该地区一共有多少个城市
	for(var i=1;i<apid.length;i++)
	{		
		if(apid[i] == ppid  )//如果该城市属于该地区
		{			
			cityIndex = cityIndex +1 ;		

			var opt = new Option(aname[i],acid[i]) ;
			if(selectedcity!='undefind'&&selectedcity==acid[i]){//数据回填	
				opt.selected = true ;	
			}else{
				if(acid[i] == bigCityNum )//如果是省会，则选中
					opt.selected = true ;	
			}
			listobj.options[listobj.options.length] = opt ;
			
			opt = null ;
		}
	}
	
	/*
	//这是原来的代码，被yangzi 注释掉了
	if(listobj.options.length == 2)//如果只有一个城市
	{
		listobj.options[1].selected = true ;
	}
	*/
}

function clearOption(obj)
{
	while(obj.options.length>0)
	{
		obj.options[obj.options.length-1] = null ;
	}
}
function selectedProv(obj,pid)
{
	for(var i=0;i<obj.options.length;i++)
	{
		if(obj.options[i].value == pid)
		{
			obj.options[i].selected = true ;
			break ;
		}
	}
}
function selectedCity(listobj,pid,cid)
{
	showCity(pid,listobj) ;
	obj = listobj ;
	for(var i=0;i<obj.options.length;i++)
	{
		if(obj.options[i].value == cid)
		{
			obj.options[i].selected = true ;
			break ;
		}
	}
}

var def_pid = 0;
var def_cid = 0;
var def_pname = "";
var def_cname = "";
var value1 = "";


function getareastr(areacd,value) {
    if (areacd > 0 ) {
        def_cid = areacd ;
        for (i = 0 ; i < acid.length ; i++) {
            if (acid[i] == areacd) {
                def_cname = aname[i];
                def_pid = apid[i];
                for (j = 0 ; j < pid.lengrh ; j++) {
                    if (pid[j] == def_pid) {
                        def_pname = pname[j];
                        break;
                    }
                }
                break;
            }
        }
    }
    value1 = def_pname + def_cname;
}

var def_pname1="";
var vbalue11="";
function getprostr(areacd,value) {
    if (areacd > 0 ) {                  
                for (j = 0 ; j < pid.length ; j++) {
                    if (pid[j] == areacd) {
                        def_pname1 = pname[j];
                        break;
                    }
                }                                 
    			}
    value11 = def_pname1 ;
}


function getareainfo(areacd) {
    if (areacd > 0 ) {
        def_cid = areacd ;
        for (i = 0 ; i < acid.length ; i++) {
            if (acid[i] == areacd) {
                def_cname = aname[i];
                def_pid = apid[i];
                for (j = 0 ; j < pid.lengrh ; j++) {
                    if (pid[j] == def_pid) {
                        def_cname = pname[j];
                        break;
                    }
                }
                break;
            }
        }
    }
}
