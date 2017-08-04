import matplotlib as mpl
import matplotlib.pyplot as plt
import os
import sys
import brewer2mpl
bmap = brewer2mpl.get_map('Set2', 'qualitative', 7)
colors = bmap.mpl_colors
mpl.rcParams['axes.color_cycle'] = colors

def main(f):
    normal = []
    attack = []
    data = open(f).read().split('\n')
    for line in data:
        if len(line) == 0:
            continue
        nums = line.split(' ')
        assert len(nums) == 7
        n = int(nums[0]) + int(nums[1]) + int(nums[2]) + int(nums[3])
        a = int(nums[4]) + int(nums[5])
        normal.append(n)
        attack.append(a)
    assert len(normal) == len(attack)
    x = range(0, len(normal))

    plt.figure(1)
    ax = plt.subplot(111)
    plt.plot(x, normal, '-', label='normal', linewidth=3)
    plt.plot(x, attack, '-', label='attack', linewidth=3)

    plt.xlabel('Time(s)')
    plt.ylabel('Entry Number')

    legend = plt.legend(loc='upper right', shadow=False, fontsize='medium')
    plt.show()
    # plt.savefig('1.pdf')

if __name__ == '__main__':
    main(sys.argv[1])
