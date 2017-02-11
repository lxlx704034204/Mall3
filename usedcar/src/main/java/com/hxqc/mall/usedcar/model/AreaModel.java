package com.hxqc.mall.usedcar.model;

import com.google.gson.annotations.Expose;

import java.util.ArrayList;

/**
 * @Author : 钟学东
 * @Since : 2015-08-25
 * FIXME
 * Todo
 */
public class AreaModel {
    /**
     * p_code : 420000
     * p_name : 湖北省
     * short_spell : HB
     * city : [{"c_code":"420100","c_name":"武汉市","region":[{"r_code":"420101","r_name":"市辖区"},{"r_code":"420102","r_name":"江岸区"},{"r_code":"420103","r_name":"江汉区"},
     * {"r_code":"420104","r_name":"硚口区"},{"r_code":"420105","r_name":"汉阳区"},{"r_code":"420106","r_name":"武昌区"},{"r_code":"420107","r_name":"青山区"},
     * {"r_code":"420111","r_name":"洪山区"},{"r_code":"420112","r_name":"东西湖区"},{"r_code":"420113","r_name":"汉南区"},{"r_code":"420114","r_name":"蔡甸区"},
     * {"r_code":"420115","r_name":"江夏区"},{"r_code":"420116","r_name":"黄陂区"},{"r_code":"420117","r_name":"新洲区"}]},{"c_code":"420200","c_name":"黄石市",
     * "region":[{"r_code":"420201","r_name":"市辖区"},{"r_code":"420202","r_name":"黄石港区"},{"r_code":"420203","r_name":"西塞山区"},{"r_code":"420204","r_name":
     * "下陆区"},{"r_code":"420205","r_name":"铁山区"},{"r_code":"420222","r_name":"阳新县"},{"r_code":"420281","r_name":"大冶市"}]},{"c_code":"420300","c_name":"十堰市",
     * "region":[{"r_code":"420301","r_name":"市辖区"},{"r_code":"420302","r_name":"茅箭区"},{"r_code":"420303","r_name":"张湾区"},{"r_code":"420321","r_name":"郧　县"},
     * {"r_code":"420322","r_name":"郧西县"},{"r_code":"420323","r_name":"竹山县"},{"r_code":"420324","r_name":"竹溪县"},{"r_code":"420325","r_name":"房　县"},{"r_code":"420381",
     * "r_name":"丹江口市"}]},{"c_code":"420500","c_name":"宜昌市","region":[{"r_code":"420501","r_name":"市辖区"},{"r_code":"420502","r_name":"西陵区"},
     * {"r_code":"420503","r_name":"伍家岗区"},{"r_code":"420504","r_name":"点军区"},{"r_code":"420505","r_name":"猇亭区"},{"r_code":"420506","r_name":"夷陵区"},
     * "r_code":"420525","r_name":"远安县"},{"r_code":"420526","r_name":"兴山县"},{"r_code":"420527","r_name":"秭归县"},{"r_code":"420528","r_name":"长阳土家族自治县"},
     * {"r_code":"420529","r_name":"五峰土家族自治县"},{"r_code":"420581","r_name":"宜都市"},{"r_code":"420582","r_name":"当阳市"},{"r_code":"420583","r_name":"枝江市"}]},
     * {"c_code":"420600","c_name":"襄樊市","region":[{"r_code":"420601","r_name":"市辖区"},{"r_code":"420602","r_name":"襄城区"},{"r_code":"420606","r_name":"樊城区"},
     * {"r_code":"420607","r_name":"襄阳区"},{"r_code":"420624","r_name":"南漳县"},{"r_code":"420625","r_name":"谷城县"},{"r_code":"420626","r_name":"保康县"},
     * {"r_code":"420682","r_name":"老河口市"},{"r_code":"420683","r_name":"枣阳市"},{"r_code":"420684","r_name":"宜城市"}]},
     * {"c_code":"420700","c_name":"鄂州市","region":[{"r_code":"420701","r_name":"市辖区"},{"r_code":"420702","r_name":"梁子湖区"},
     * {"r_code":"420703","r_name":"华容区"},{"r_code":"420704","r_name":"鄂城区"}]},{"c_code":"420800","c_name":"荆门市","region":[{"r_code":"420801","r_name":"市辖区"},
     * {"r_code":"420802","r_name":"东宝区"},{"r_code":"420804","r_name":"掇刀区"},{"r_code":"420821","r_name":"京山县"},{"r_code":"420822","r_name":"沙洋县"},
     * {"r_code":"420881","r_name":"钟祥市"}]},{"c_code":"420900","c_name":"孝感市","region":[{"r_code":"420901","r_name":"市辖区"},{"r_code":"420902","r_name":"孝南区"},
     * {"r_code":"420921","r_name":"孝昌县"},{"r_code":"420922","r_name":"大悟县"},{"r_code":"420923","r_name":"云梦县"},{"r_code":"420981","r_name":"应城市"},
     * {"r_code":"420982","r_name":"安陆市"},{"r_code":"420984","r_name":"汉川市"}]},{"c_code":"421000","c_name":"荆州市","region":[{"r_code":"421001","r_name":"市辖区"},
     * {"r_code":"421002","r_name":"沙市区"},{"r_code":"421003","r_name":"荆州区"},{"r_code":"421022","r_name":"公安县"},{"r_code":"421023","r_name":"监利县"},
     * {"r_code":"421024","r_name":"江陵县"},{"r_code":"421081","r_name":"石首市"},{"r_code":"421083","r_name":"洪湖市"},{"r_code":"421087","r_name":"松滋市"}]},
     * {"c_code":"421100","c_name":"黄冈市","region":[{"r_code":"421101","r_name":"市辖区"},{"r_code":"421102","r_name":"黄州区"},{"r_code":"421121","r_name":"团风县"},
     * {"r_code":"421122","r_name":"红安县"},{"r_code":"421123","r_name":"罗田县"},{"r_code":"421124","r_name":"英山县"},{"r_code":"421125","r_name":"浠水县"},
     * {"r_code":"421126","r_name":"蕲春县"},{"r_code":"421127","r_name":"黄梅县"},{"r_code":"421181","r_name":"麻城市"},{"r_code":"421182","r_name":"武穴市"}]},
     * {"c_code":"421200","c_name":"咸宁市","region":[{"r_code":"421201","r_name":"市辖区"},{"r_code":"421202","r_name":"咸安区"},{"r_code":"421221","r_name":"嘉鱼县"},
     * {"r_code":"421222","r_name":"通城县"},{"r_code":"421223","r_name":"崇阳县"},{"r_code":"421224","r_name":"通山县"},{"r_code":"421281","r_name":"赤壁市"}]},
     * {"c_code":"421300","c_name":"随州市","region":[{"r_code":"421301","r_name":"市辖区"},{"r_code":"421302","r_name":"曾都区"},{"r_code":"421381","r_name":"广水市"}]},
     * {"c_code":"422800","c_name":"恩施土家族苗族自治州","region":[{"r_code":"422801","r_name":"恩施市"},{"r_code":"422802","r_name":"利川市"},{"r_code":"422822","r_name":"建始县"},
     * {"r_code":"422823","r_name":"巴东县"},{"r_code":"422825","r_name":"宣恩县"},{"r_code":"422826","r_name":"咸丰县"},{"r_code":"422827","r_name":"来凤县"},
     * {"r_code":"422828","r_name":"鹤峰县"}]}]
     */
    @Expose
    public String p_code;
    @Expose
    public String p_name;
    @Expose
    public String short_spell;
    @Expose
    public String p_id;
    @Expose
    public ArrayList<CityModel> city;
}
